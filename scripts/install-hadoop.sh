#!/bin/bash

set -e

echo "Starting full Hadoop installation..."

# Step 1: Update package lists and install dependencies
echo ""
echo "[Step 1] Updating packages and installing dependencies..."
sudo apt update
sudo apt install -y openjdk-11-jdk wget tar ssh pdsh openssh-server

# Step 2: Download and extract Hadoop
echo ""
echo "[Step 2] Downloading and extracting Hadoop..."
cd ~
wget -c https://dlcdn.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
rm -rf ~/hadoop
mkdir -p ~/hadoop
tar --strip-components=1 -xvzf hadoop-3.3.6.tar.gz -C ~/hadoop
rm -f ~/hadoop-3.3.6.tar.gz

# Step 3: Set environment variables
echo ""
echo "[Step 3] Setting up environment variables..."
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export HADOOP_HOME=~/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

{
echo ""
echo "# Hadoop Environment Variables"
echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64"
echo "export HADOOP_HOME=\$HOME/hadoop"
echo "export HADOOP_CONF_DIR=\$HADOOP_HOME/etc/hadoop"
echo "export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin"
echo "# Hadoop Management Scripts"
echo "export PATH=\$HOME/hadoop-scripts:\$PATH"
} >> ~/.bashrc

# Step 4: Create management scripts folder
echo ""
echo "[Step 4] Creating management scripts folder..."
mkdir -p ~/hadoop-scripts

# Step 5: Create HDFS data directories
echo ""
echo "[Step 5] Creating Hadoop HDFS data directories..."
mkdir -p ~/hadoopdata/hdfs/namenode
mkdir -p ~/hadoopdata/hdfs/datanode

# Step 6: Configure Hadoop XML files
echo ""
echo "[Step 6] Configuring Hadoop XML files..."

NAMENODE_DIR="file://$HOME/hadoopdata/hdfs/namenode"
DATANODE_DIR="file://$HOME/hadoopdata/hdfs/datanode"

cat > $HADOOP_CONF_DIR/core-site.xml <<EOL
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://localhost:9000</value>
  </property>
</configuration>
EOL

cat > $HADOOP_CONF_DIR/hdfs-site.xml <<EOL
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>$NAMENODE_DIR</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>$DATANODE_DIR</value>
  </property>
</configuration>
EOL

cat > $HADOOP_CONF_DIR/mapred-site.xml <<EOL
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
  <property>
    <name>yarn.app.mapreduce.am.env</name>
    <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
  <property>
    <name>mapreduce.map.env</name>
    <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
  <property>
    <name>mapreduce.reduce.env</name>
    <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
</configuration>
EOL

cat > $HADOOP_CONF_DIR/yarn-site.xml <<EOL
<configuration>
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
</configuration>
EOL

# Step 7: Update JAVA_HOME inside Hadoop config
echo ""
echo "[Step 7] Updating JAVA_HOME inside hadoop-env.sh..."
sed -i "s|^\# export JAVA_HOME=.*|export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64|" $HADOOP_CONF_DIR/hadoop-env.sh

# Step 8: Set up SSH for Hadoop
echo ""
echo "[Step 8] Setting up passwordless SSH..."
sudo service ssh start
ssh-keygen -t rsa -P "" -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
chmod 700 ~/.ssh

# Step 9: Format HDFS
echo ""
echo "[Step 9] Checking if HDFS needs formatting..."
if ! jps | grep -q NameNode; then
    echo "No running NameNode detected. Formatting HDFS..."
    hdfs namenode -format
else
    echo "NameNode process is running. Skipping format."
fi

# Step 10: Create management scripts
echo ""
echo "[Step 10] Creating Hadoop management scripts..."

cat > ~/hadoop-scripts/hadoop-start.sh <<EOL
#!/bin/bash
echo "Starting Hadoop services manually..."

sudo service ssh start
rm -f /tmp/hadoop-\$(whoami)-resourcemanager.pid

echo "Starting NameNode..."
hdfs --daemon start namenode

echo "Starting DataNode..."
hdfs --daemon start datanode

echo "Starting ResourceManager..."
yarn --daemon start resourcemanager

echo "Starting NodeManager..."
yarn --daemon start nodemanager

echo "All Hadoop services started!"
EOL

cat > ~/hadoop-scripts/hadoop-stop.sh <<EOL
#!/bin/bash
echo "Stopping Hadoop services manually..."

echo "Stopping NodeManager..."
yarn --daemon stop nodemanager || true

echo "Stopping ResourceManager..."
yarn --daemon stop resourcemanager || true

echo "Stopping DataNode..."
hdfs --daemon stop datanode || true

echo "Stopping NameNode..."
hdfs --daemon stop namenode || true

sleep 3

echo "Checking for leftover Hadoop Java processes..."
LEFTOVER=\$(jps | grep -E 'NameNode|DataNode|ResourceManager|NodeManager' || true)

if [ -n "\$LEFTOVER" ]; then
    echo "Leftover Hadoop processes detected:"
    echo "\$LEFTOVER"
    echo "Force killing all Hadoop-related Java processes..."
    pkill -f hadoop || true
    pkill -f java || true
    sleep 2
else
    echo "No leftover Hadoop processes found."
fi

echo "Final process check:"
jps

echo "All Hadoop services fully stopped and cleaned."
EOL

cat > ~/hadoop-scripts/hadoop-check.sh <<EOL
#!/bin/bash
echo "Checking Hadoop services..."

jps

echo ""
echo "If you see NameNode, DataNode, ResourceManager, NodeManager -> GOOD"
echo "If missing any -> Something is wrong, check logs or restart."
EOL

cat > ~/hadoop-scripts/hadoop-restart.sh <<EOL
#!/bin/bash
echo "Restarting Hadoop services..."

~/hadoop-scripts/hadoop-stop.sh
sleep 3
~/hadoop-scripts/hadoop-start.sh

echo "Hadoop services restarted!"
EOL

chmod +x ~/hadoop-scripts/*.sh

# Step 11: Final instructions
echo ""
echo "Hadoop setup completed successfully!"
echo ""
echo "To start Hadoop:       hadoop-start.sh"
echo "To stop Hadoop:        hadoop-stop.sh"
echo "To check services:     hadoop-check.sh"
echo "To restart Hadoop:     hadoop-restart.sh"
echo ""
echo "IMPORTANT: Please run the following to refresh your environment:"
echo "    source ~/.bashrc"
echo ""

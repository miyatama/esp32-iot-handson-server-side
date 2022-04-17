sudo tee /etc/systemd/system/zookeeper.service << EOF
[Unit]
Description=Zookeeper Service
Documentation=
After=networking.service

[Service]
Type=simple
User=$(whoami)
Group=$(whoami)
TimeoutStartSec=0
Restart=on-failure
RestartSec=30s
#ExecStartPre=
ExecStart=/home/$(whoami)/run_zookeeper.sh
SyslogIdentifier=Zookeeper
#ExecStop=

[Install]
WantedBy=multi-user.target
EOF

sudo tee /etc/systemd/system/kafka.service << EOF
[Unit]
Description=kafka Service
Documentation=
After=networking.service

[Service]
Type=simple
User=$(whoami)
Group=$(whoami)
TimeoutStartSec=0
Restart=on-failure
RestartSec=30s
#ExecStartPre=
ExecStart=/home/$(whoami)/run_kafka.sh
SyslogIdentifier=Kafka
#ExecStop=

[Install]
WantedBy=multi-user.target
EOF

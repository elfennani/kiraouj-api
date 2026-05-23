#!/bin/bash

set -e

APP_NAME="kiraouj"
DOMAIN="api.elfenn.me"
EMAIL="elfennani.nizar@gmail.com"
APP_USER="root"
APP_DIR="/opt/$APP_NAME"
JAR_PATH="$APP_DIR/app.jar"

echo "Updating system..."
apt update && apt upgrade -y

echo "Installing dependencies..."
apt install -y \
  openjdk-17-jdk \
  nginx \
  curl \
  unzip \
  git \
  certbot python3-certbot-nginx

echo "Creating app directory..."
mkdir -p $APP_DIR

echo "Setting permissions..."
chown -R root:root $APP_DIR
chmod -R 755 $APP_DIR

echo "Creating systemd service..."

cat > /etc/systemd/system/$APP_NAME.service <<EOF
[Unit]
Description=Kiraouj API App ($APP_NAME)
After=network.target

[Service]
User=root
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java -jar $JAR_PATH
Restart=always
RestartSec=5
SuccessExitStatus=143

# Optional memory tuning
Environment="JAVA_OPTS=-Xms256m -Xmx512m"
Environment="DATABASE_URL=jdbc:sqlite:$APP_DIR/data.db"

[Install]
WantedBy=multi-user.target
EOF

echo "Reloading systemd..."
systemctl daemon-reload
systemctl enable $APP_NAME

echo "Configuring Nginx..."

cat > /etc/nginx/sites-available/$APP_NAME <<EOF
server {
    listen 80;
    server_name $DOMAIN;

    location / {
        proxy_pass http://localhost:8080;

        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

ln -sf /etc/nginx/sites-available/$APP_NAME /etc/nginx/sites-enabled/

rm -f /etc/nginx/sites-enabled/default || true

nginx -t
systemctl restart nginx

echo "Obtaining SSL certificate..."
certbot --nginx -d $DOMAIN --non-interactive --agree-tos -m $EMAIL --redirect || true
systemctl reload nginx


echo "Creating placeholder jar file..."
touch $JAR_PATH

echo "Setup complete."

echo "Next steps:"
echo "1. Upload your jar to: $JAR_PATH"
echo "2. Restart service: systemctl restart $APP_NAME"
echo "3. Check logs: journalctl -u $APP_NAME -f"
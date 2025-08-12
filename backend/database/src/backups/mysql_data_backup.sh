#!/bin/sh
set -e

TIMESTAMP=$(date +'%Y%m%d%H%M%S')
BACKUP_DIR="./local_backups"
BACKUP_FILE="mysql_data_backup_${TIMESTAMP}.tar.gz"

# Ensure the backup directory exists
mkdir -p "$BACKUP_DIR"

echo "📦 Backing up MySQL volume from running container..."

# Run the backup process inside the running MySQL container
docker exec GeoTechnicalDatabase sh -c "tar -czf - -C /var/lib/mysql ." > "$BACKUP_DIR/$BACKUP_FILE"

# Check if the file was actually created before proceeding
if [ -f "$BACKUP_DIR/$BACKUP_FILE" ]; then
  chmod 600 "$BACKUP_DIR/$BACKUP_FILE"
  echo "Backup saved to $BACKUP_FILE"
else
  echo "Backup failed. No file created."
  exit 1
fi

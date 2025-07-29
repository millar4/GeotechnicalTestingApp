#!/bin/bash
echo "Backing up MySQL database..."
docker exec GeoTechnicalDatabase mysqldump -u root -prootpassword GeotechnicalTests > ./backend/database/latest_backup.sql
echo "Backup completed."

#MongoDB
Steps:
1. Download installation files from official site
2. Install mongod server
3. Add path to folder to Environment variables
4. Add folder for db and logs
5. Add config file like the following

        systemLog:
            destination: file
            path: c:\mongodb\log\mongod.log
        storage:
            dbPath: c:\mongodb\data\db
6. Install mongo db as service
        mongod --config "C:\mongodb\mongod.cfg" --install
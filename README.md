## Database Configuration

To connect the project to your own MySQL database, update the following file:

### Steps:

1. Create the database:  
   CREATE DATABASE courier_db;
   
2. Update the `config.properties` file:
   - `db.url`: Your JDBC URL (with DB name)
   - `db.user`: Your MySQL username
   - `db.password`: Your MySQL password

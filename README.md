## About Library
To create a file-driven database , initally we have to create an instance of FileDB along with the filepath else the file will be created in a default path.
For Example,
        FileDB a = new FileDB("C:/Users/nisha/Desktop/Freshworks/DataBaseFile.txt");

As of now, the library supports 3 functions namely create, read and delete .

## About functions
1. The function "create" is used to create a key-value pair. The parameters that need to be passed are key in a String format, value which is a JSON Object and TTL (how much time should the pair be in database) which will take a long integer.      
2. The function "read" is used to get a value, for a given key, from the file if it exists. The parameter key in String format need to be sent to the function.
3. The function "delete" is used to delete the key-value pair, if it exists in the database. The parameter key needs to be sent in String format.

Note : 
1. The key is Case Sensitive
2. One can not create, read or delete when another creation or deletion is in progress.
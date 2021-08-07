# Multithreaded-JSON-Database-Server

This project includes a server that controls a database based on Maps and Json, allowing GET, SET and DELETE operations.
The server uses executors to control multiple threads. It takes advantage of the command pattern to organize requests.
This project also includes a client that read requests from the command line or a file and send them to the server using
Sockets. 

## Requests
- The available request types are: get, set, delete, and exit.
- To specify the key to the record, the user should type the full path to this field in a form of a JSON array.
- The value can be either a string, or a JSON object.

## Examples

```
> java Main -t set -k 1 -v "Hello world!" 
Client started!
Sent: {"type":"set","key":"1","value":"Hello world!"}
Received: {"response":"OK"}
```

```
> java Main -in setFile.json 
Client started!
Sent:
{
   "type":"set",
   "key":"person",
   "value":{
      "name":"Elon Musk",
      "car":{
         "model":"Tesla Roadster",
         "year":"2018"
      },
      "rocket":{
         "name":"Falcon 9",
         "launches":"87"
      }
   }
}
Received: {"response":"OK"}
```

```
> java Main -in getFile.json 
Client started!
Sent: {"type":"get","key":["person","name"]}
Received: {"response":"OK","value":"Elon Musk"}
```

```
> java Main -in updateFile.json 
Client started!
Sent: {"type":"set","key":["person","rocket","launches"],"value":"88"}
Received: {"response":"OK"}
```

```
> java Main -in secondGetFile.json 
Client started!
Sent: {"type":"get","key":["person"]}
Received:
{
   "response":"OK",
   "value":{
      "name":"Elon Musk",
      "car":{
         "model":"Tesla Roadster",
         "year":"2018"
      },
      "rocket":{
         "name":"Falcon 9",
         "launches":"88"
      }
   }
}
```

```
> java Main -in deleteFile.json 
Client started!
Sent: {"type":"delete","key":["person","car","year"]}
Received: {"response":"OK"}
```

```
> java Main -in secondGetFile.json 
Client started!
Sent: {"type":"get","key":["person"]}
Received:
{
   "response":"OK",
   "value":{
      "name":"Elon Musk",
      "car":{
         "model":"Tesla Roadster"
      },
      "rocket":{
         "name":"Falcon 9",
         "launches":"88"
      }
   }
}
```

```
> java Main -t exit 
Client started!
Sent: {"type":"exit"}
Received: {"response":"OK"}
```
# Rest Log4 Android
![Overview](https://github.com/rochaeinar/RestLog4Android/blob/master/ContextOverview.png)

* Add the JitPack repository to your "build.gradle" (project file) at the end of repositories:

		allprojects {
			repositories {
			        //..
			        maven { url "https://jitpack.io" }
			}
		}

* Add the dependency in "build.gradle" (module file)

		dependencies {
		        implementation 'com.github.rochaeinar:RestLog4Android:1.0.0'
		}
    
    
# Basic Usage
		 Log.w("RestLog", "Hello", "World");
    
    
# Advanced usage
## Add a Json configuration file: asets/rest-log.json

```
{
  "enabled": true,
  "avoidDuplicated": false,
  "maxFileSizeMb": 3,
  "maxRecordNumber": 100,
  "level": "verbose",
  "deleteAfter": 30,
  "appenders": [
    {
      "type": "rest",
      "host": "http://localhost:3000"
    },
    {
      "type": "console"
    },
    {
      "type": "logcat"
    },
    {
      "type": "database",
      "nameFormat": "yyyy-MM-dd",
      "path": "logs"
    },
    {
      "type": "file",
      "nameFormat": "yyyy-MM-dd",
      "format": "txt",
      "path": "logs"
    }
  ],
  "filters": [
    {
      "field": "message",
      "value": "h",
      "filterOperator": "contains"
    }
  ]
}
```
    

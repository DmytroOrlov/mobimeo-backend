Build:
```sh
$ sbt stage
```
Run:
```sh
$ target/universal/stage/bin/mobimeo-backend -Dhttp.port=8081 -Dplay.http.secret.key=abcdefghijk
```
Find a vehicle for a given time=10:03:00 and X=1 & Y=4 coordinates:
```sh
$ curl http://localhost:8081/find/10:03:00/1/4
```
Return the vehicle arriving next at a given stop
```sh
$ curl http://localhost:8081/next/0
```
Indicate if a given line is currently delayed
```sh
$ http://localhost:8081/isDelayed/200
```

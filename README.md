# Hak

This is a project that builds a spring boot application that downloads
and splits full albums from youtube and writes it to a mountable folder.

### Building

###### docker
First, build the docker image using the `dockerBuildImage` gradle task.
This will create a docker image on your system that you can then use for
example in a docker-compose file.
An example docker-compose file can be found in `/src/main/docker/`.

###### jar

If you just want the spring boot runnable jar, execute the `bootJar`
gradle task. This will create the jar in `/build/libs/`.

### Usage

###### docker

Start the container with the docker-compose file at `/src/main/docker/`
with `docker-compose up -d`. You now have a running container with port
`8081`exposed to make POST requests to.
An example request looks like this:

`curl -v -XPOST localhost:8081/albumdownload/download -d
'{
    "url":"https://www.youtube.com/watch?v=JI5Nxc8yUzA",
    "artist":"SerpentSound Studios",
    "album":"Fjörður",
    "trackInfoString":
       "00:00:00 - Grundar,
        00:04:32 - Vopna,
        00:12:08 - Norður,
        00:15:53 - Borgar,
        00:21:28 - Skaga,
        00:27:03 - Mjói,
        00:30:32 - Stöðvar,
        00:34:46 - Góða Nótt"
}'
-H "Content-Type: application/json"
`

After the service has processed the request, the songs should be
available in your `/tmp/mounted/` folder by default.

### Future work
The following improvements are desired and will be made soon enough.
Do not hesitate to create issues for other improvements or send a PR if
you want to contribute.

##### Make example frontend
Make an example frontend that makes the POST request (in separate repo).
Manually editing requests is tedious.

##### Ditch system calls to youtube-dl and ffmpeg for an actual java implementation
Or at least filter out attempts to execute arbitrary code execution
before processing the request body.

###### Make stuff configurable
e.g. option to group albums per artist in a folder, configurable logging
, youtube-dl parameters, ffmpeg parameters, ...

###### Better trackInfo format
This can be a breaking change due to changing api parameters.




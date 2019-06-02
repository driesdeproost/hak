# Hak
A Spring boot application that downloads and splits full albums from youtube and writes it to a mountable folder.

Be careful where you deploy this for now, as it currently calls the shell where it runs.

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
-H "Content-Type: application/json"`

After the service has processed the request, the songs should be
available in your `/tmp/mounted/` folder by default.

You can also use the example web interface at port 8080.

###### Jar/bootrun

You can just run the java jar or run the gradle bootrun task. This will expose all endpoints on port 8080 by default.
`

### Contribution

Do not hesitate to create issues or send a PR if you want to contribute.




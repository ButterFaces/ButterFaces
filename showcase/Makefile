docker-build:
	mvn clean package -Pshowcase-in-docker -Dmaven.test.skip=true

docker-run:
	docker run -d -p 8080:8080 --rm --name butterfaces butterfaces/showcase:latest

docker-stop:
	docker stop butterfaces
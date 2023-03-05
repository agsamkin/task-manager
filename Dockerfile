FROM gradle:7.6-jdk17

WORKDIR /app

COPY ./ .

RUN gradle installDist

RUN gradle assembleFrontend
RUN gradle installFrontend

CMD ./build/install/app/bin/app
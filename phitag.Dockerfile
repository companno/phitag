FROM debian:11
SHELL ["/bin/bash", "--login", "-c"]
EXPOSE 3000/tcp
EXPOSE 3000/udp
EXPOSE 5432/tcp
EXPOSE 5432/udp
EXPOSE 8080/tcp
EXPOSE 8080/udp
RUN apt -y update
RUN apt -y install apt-transport-https ca-certificates curl gnupg2 software-properties-common python3 pip
RUN apt -y install openjdk-17-jre-headless
RUN apt -y install maven
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list
RUN apt -y update
RUN apt -y install yarn
#RUN curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.35.3/install.sh | bash
RUN nvm install 14

WORKDIR /phitag/backend
COPY pom.xml /phitag/pom.xml
COPY backend/pom.xml .
COPY backend/src ./src
COPY backend/target ./target
RUN mvn install

WORKDIR /phitag/frontend
COPY frontend/components ./components
COPY frontend/lib ./lib
COPY frontend/pages ./pages
COPY frontend/public ./public
COPY frontend/styles ./styles
COPY frontend/.eslintrc.json .
COPY frontend/next-env.d.ts .
COPY frontend/next.config.js .
COPY frontend/package.json .
COPY frontend/postcss.config.js .
COPY frontend/tailwind.config.js .
COPY frontend/tsconfig.json .
RUN yarn install

CMD bash -c "cd /phitag/backend && mvn spring-boot:run & sleep 10 && cd /phitag/frontend && yarn dev"

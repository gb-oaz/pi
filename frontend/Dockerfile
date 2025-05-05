FROM node:22

WORKDIR /app

COPY . .

RUN yarn install --frozen-lockfile

EXPOSE 3000 3001

CMD ["sh", "-c", "yarn prod:start"]
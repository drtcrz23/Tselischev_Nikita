CREATE TABLE comment (
    id BIGSERIAL PRIMARY KEY,
    "articleId" BIGSERIAL REFERENCES article(id) NOT NULL,
    text TEXT NOT NULL
);
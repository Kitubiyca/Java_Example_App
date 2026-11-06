const appDb = db.getSiblingDB("example_app");

appDb.createUser({
  user: "app_user",
  pwd: "app_pass",
  roles: [{ role: "readWrite", db: "example_app" }]
});

assert.commandWorked(appDb.createCollection("user_favorites", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["values"],
      properties: {
        values: {
          bsonType: "array",
          maxItems: 50,
          items: {
            bsonType: "binData",
            description: "UUID v4 в бинарном виде"
          },
        }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
}));

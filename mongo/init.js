const db = db.getSiblingDB("example_app");

db.createUser({
  user: "app_user",
  pwd: "app_pass",
  roles: [{ role: "readWrite", db: "example_app" }]
});

db.createCollection("user_favorites", {
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
            subType: 4,
            description: "UUID v4 в бинарном виде"
          },
        }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
})

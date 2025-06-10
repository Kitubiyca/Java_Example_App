const db = db.getSiblingDB("example_app");

db.createCollection("user_favorites", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["values"],
      properties: {
        values: {
          bsonType: "array",
          maxItems: 50,
          items: { bsonType: "string" },
        }
      }
    }
  },
  validationLevel: "strict",
  validationAction: "error"
})

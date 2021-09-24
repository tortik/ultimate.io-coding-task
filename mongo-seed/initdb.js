let error = true

let res = [
  db.replyData.drop(),
  db.replyData.createIndex({ intent: 1 }, { unique: true }),
  db.replyData.insertMany([
    {
      "intent":  "greeting",
      "reply" : "Hello. How can I help you",
      "threshold": 10.0
    },
    {
      "intent":  "goodbye",
      "reply" : "Thank you for a conversation. Goodbye.",
      "threshold": 20.0
    },
    {
      "intent":  "thank you",
      "reply" : "You are welcome. Can I help you more?",
      "threshold": 100
    }
  ]),
]

printjson(res)

if (error) {
  print('Error, exiting')
  quit(1)
}

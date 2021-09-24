print('Initializing DB Start');

db.replyData.drop();
db.replyData.insertMany([
    {
        "intent": "greeting",
        "reply": "Hello. How can I help you",
        "threshold": 0.6
    },
    {
        "intent": "goodbye",
        "reply": "Thank you for a conversation. Goodbye.",
        "threshold": 0.6
    },
    {
        "intent": "thank you",
        "reply": "You are welcome. Can I help you more?",
        "threshold": 0.9
    }
]);

print('Initializing DB End');

from flask import Flask, request
from pymongo import MongoClient

client = MongoClient('localhost', 27017)
db = client.db
tasks = db.tasks
app = Flask(__name__)

@app.route('/put_command')
def put_command():
	print "put_command called!"
	command = request.form['command']
	task = {"command": command,
			"timestamp": time.time(),
			"used": False}
	task_id = collection.insert(task)
	print command
	return "Put Command"

@app.route('/get_command')
def get_command():
	print "get_command called!"
	ret = db.tasks.find_one({used: False},sort=[("timestamp", pymongo.ASCENDING)])
	return ret

if __name__ == '__main__':
    app.run(host='0.0.0.0')

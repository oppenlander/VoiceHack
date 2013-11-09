from flask import Flask, request, jsonify
from pymongo import MongoClient

client = MongoClient('localhost', 27017)
db = client.db
tasks = db.tasks
app = Flask(__name__)

@app.route('/put_task', methods=['POST'])
def put_task():
	print "put task"
	if 'command' in request.json:
		command = request.json['command']
		task = {
			"command": command,
			"timestamp": time.time(),
			"used": False
		}
		task_id = collection.insert(task)
		print command
	return "success"

@app.route('/get_task', methods=['GET'])
def get_task():
	print "get task"
	task = db.tasks.find_one({used: False},sort=[("timestamp", pymongo.ASCENDING)])
	if task:
		return jsonify(command=task.command,
			timestamp=task.timestamp,
			used=task.used)
	return "failure"

if __name__ == '__main__':
    app.run(host='0.0.0.0')

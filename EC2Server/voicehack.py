import flask
import pymongo
import json
import time

client = pymongo.MongoClient('localhost', 27017)
db = client.db
app = flask.Flask(__name__)

@app.route('/put_task', methods=['POST'])
def put_task():
	print "put task"
	if 'command' in flask.request.json:
		command = flask.request.json['command']
		task = {
			'command': command,
			'timestamp': int(time.time()),
			'used': False
		}
		task_id = db.tasks.insert(task)
		return "success"
	return "failure"

@app.route('/get_task_fake', methods=['GET'])
def get_task_fake():
	print "get task fake"
	return '{"command":"open garage door","timestamp":"0","used":"0"}'

@app.route('/get_task', methods=['GET'])
def get_task():
	print "get task"
	task = db.tasks.find_one({'used': False}, sort=[("timestamp", pymongo.ASCENDING)])
	if task:
		db.tasks.update({'_id': task['_id']}, {'used': True})
		if '_id' in task:
			# Deleting _id so it is JSON serializable
			del task['_id']
		return json.dumps(task)
	return "no tasks"

@app.route('/set_ip/<addr>')
def set_ip(addr):
	print "set ip"
	ip = {
		'ip': 'ip',
		'addr': addr,
		'timestamp': int(time.time())
	}
	ip_id = db.ip.insert(ip)
	return 'success'

@app.route('/get_ip')
def get_ip():
	ip = db.ip.find_one({'ip': 'ip'}, sort=[('timestamp', pymongo.DESCENDING)])
	if ip:
		return ip['addr']
	return 'no ip set'

if __name__ == '__main__':
    app.run(host='0.0.0.0')
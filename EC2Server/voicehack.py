from flask import Flask, request
app = Flask(__name__)

@app.route('/put_task', methods=['POST'])
def put_task():
	print "put task"
	if 'command' in request.json:
		command = request.json['command']
		print command
	return "success"

@app.route('/get_task', methods=['GET'])
def get_task():
	print "get task"
	command = 'open garage door'
	timestamp = 0
	used = 0
	ret = "{'command': %s, 'timestamp': %d, 'used': %d}" % command, timestamp, used
	print ret
	return ret

if __name__ == '__main__':
    app.run(host='0.0.0.0')
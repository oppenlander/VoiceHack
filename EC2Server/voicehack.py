from flask import Flask, request
app = Flask(__name__)

@app.route('/put_command')
def put_command():
	print "put_command called!"
	command = request.form['command']
	print command
	return "Put Command"

@app.route('/get_command')
def get_command():
	print "get_command called!"
	return "Get Command"

if __name__ == '__main__':
    app.run(host='0.0.0.0')

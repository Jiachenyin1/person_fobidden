# -*- coding=utf-8 -*-
import time
from flask import Flask,g,request,make_response,render_template,jsonify
import hashlib
import xml.etree.ElementTree as ET
import urllib2
import json
from flask import abort

app = Flask(__name__)
app.debug=True

@app.route('/',methods=['GET','POST'])
def wechat_auth():
    r = urllib2.urlopen('http://192.168.1.179:9701/lightPole/test')
    c = r.read()
    b = json.loads(c)
    d= b["task"]
    if d != None:
        #return str(d['title'])
        return d['title']
    else:
        return '空的'
tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
        'done': False
    },
    {
        'id': 2,
        'title':'丁',
        'description': u'Need to find a good Python tutorial on the web', 
        'done': False
    }
]


@app.route('/aix/<int:task_id>',methods=['GET'])
def aix(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})

if __name__ == '__main__':
    app.run()
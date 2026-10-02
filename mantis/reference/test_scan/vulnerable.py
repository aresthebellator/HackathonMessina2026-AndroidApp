import sqlite3
from flask import request

def get_user(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    # SQL Injection vulnerability
    user_id = request.args.get('id')
    cursor.execute(f"SELECT * FROM users WHERE id = {user_id}")
    return cursor.fetchall()

def execute_code():
    # Arbitrary code execution vulnerability
    code = request.args.get('code')
    eval(code)

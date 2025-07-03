from cassandra.cluster import Cluster

cluster = Cluster(["localhost"])
session = cluster.connect("spark_streams")

def get_account_by_email(email):
    query = "SELECT * FROM accounts WHERE customerid = %s ALLOW FILTERING"
    result = session.execute(query, (email,))
    return result.one()

def get_last_transactions(email, limit=3):
    account = get_account_by_email(email)
    if not account:
        return []

    query = """
        SELECT * FROM transactions 
        WHERE account_id = %s 
        LIMIT %s ALLOW FILTERING
    """
    return session.execute(query, (account.accountid, limit)).all()

def get_card_by_account(email):
    account = get_account_by_email(email)
    if not account:
        return []

    query = """
        SELECT * FROM cards 
        WHERE accountid = %s 
        ALLOW FILTERING
    """
    result = session.execute(query, (account.accountid,))
    return result.one()
     
from pyspark.sql.types import (BooleanType, DoubleType, IntegerType,
                               StringType, StructField, StructType,
                               TimestampType)

def get_transaction_schema() -> StructType:
    """Définit le schéma des transactions"""
    return StructType([
        StructField("transactionid", StringType(), False),
        StructField("accountid", StringType(), False),
        StructField("transactionamount", DoubleType(), False),
        StructField("transactiondate", TimestampType(), False),
        StructField("transactiontype", StringType(), False),
        StructField("location", StringType(), False),
        StructField("deviceid", StringType(), False),
        StructField("ipaddress", StringType(), False),
        StructField("merchantid", StringType(), False),
        StructField("accountbalance", DoubleType(), False),
        StructField("previoustransactiondate", TimestampType(), False),
        StructField("channel", StringType(), False),
        StructField("customerage", IntegerType(), False),
        StructField("customeroccupation", StringType(), False),
        StructField("transactionduration", IntegerType(), False),
        StructField("loginattempts", IntegerType(), False)
    ])
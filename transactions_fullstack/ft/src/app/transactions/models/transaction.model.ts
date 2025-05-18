import { TransactionType } from "./transaction-type.enum";

export interface Transaction {
  accountId: string;
  userEmail: string;
  transactionAmount: number;
  transactionType: TransactionType;
  location?: string;
  deviceId?: string;
  ipAddress?: string;
  merchantId?: string;
  channel?: string;
  customerAge?: number;
  customerOccupation?: string;
  transactionDuration?: number;
  loginAttempts?: number;
  accountBalance?: number;
}
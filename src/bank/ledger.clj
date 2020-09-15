(ns bank.ledger)

(defn transaction [ledger type account-id amount]
   (let [id (count ledger)
         operation {:id id :type type :account-id account-id :amount amount}]
     (conj ledger operation)))

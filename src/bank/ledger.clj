(ns bank.ledger)

(defn account [ledger type account-id]
   (let [id (count ledger)
         operation {:id id :type type :account-id account-id}]
     (conj ledger operation)))

(defn transaction [ledger type account-id amount]
   (let [id (count ledger)
         operation {:id id :type type :account-id account-id :amount amount}]
     (conj ledger operation)))

(defn transfer [ledger debit-id credit-id amount]
   (let [id (count ledger)
         operation {:id id :type :transfer :debit-id debit-id :credit-id credit-id :amount amount}]
     (conj ledger operation)))

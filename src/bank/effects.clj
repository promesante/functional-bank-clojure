(ns bank.effects
  (:require [mount.core :as mount]
            [bank.operations :as o]
            [bank.ledger :as l]))

(defn init []
  (ref
   {:accounts
    [{:id 0 :curr :us :balance 100 :state :active}
     {:id 1 :curr :eu :balance 300 :state :active}
     {:id 2 :curr :us :balance 500 :state :inactive}
    ]
    :ledger
    [{:id 0 :type :create :account-id 0}
     {:id 1 :type :create :account-id 1}
     {:id 2 :type :create :account-id 2}
     {:id 3 :type :deposit :account-id 0 :amount 200}
     {:id 4 :type :deposit :account-id 1 :amount 400}
     {:id 5 :type :deposit :account-id 2 :amount 1200}
     {:id 6 :type :withdraw :account-id 0 :amount 100}
     {:id 7 :type :withdraw :account-id 1 :amount 200}
     {:id 8 :type :withdraw :account-id 2 :amount 600}
     {:id 9 :type :transfer :debit-id 2 :credit-id 1 :amount 100}
     {:id 10 :type :delete :account-id 2}
     ]}
   )
  )

(mount/defstate registry :start (init))

;; Transactions

(defn deposit [id amount]
  (dosync
   (alter registry update :accounts o/deposit id amount)
   (alter registry update :ledger l/transaction :deposit id amount)
   )
  "success")

(defn withdraw [id amount]
  (dosync
   (alter registry update :accounts o/withdraw id amount)
   (alter registry update :ledger l/transaction :withdraw id amount)
   )
  "success")

(defn transfer [debit-id credit-id amount]
  (dosync
   (alter registry update :accounts o/withdraw debit-id amount)
   (alter registry update :accounts o/deposit credit-id amount)
   (alter registry update :ledger l/transfer debit-id credit-id amount)
   )
  "success")

;; Account Creation and Deletion

(defn account-create [curr]
  (dosync
   (let [accounts (:accounts @registry)
         id (count accounts)
         account {:id id :curr curr :balance 0 :state :active}]
     (alter registry update-in [:accounts] conj account)
     (alter registry update :ledger l/account :create id)
     ))
  "success")

(defn account-delete [id]
  (dosync
   (let [accounts (:accounts @registry)]
     (alter registry update :accounts o/set-state id :inactive)
     (alter registry update :ledger l/account :delete id))
   )
  "success")

;; Retrieving Data

(defn balance [id]
  (let [accounts (:accounts @registry)
        accounts-by-id (filter (fn [account] (= id (:id account))) accounts)]
    (if (empty? accounts-by-id)
      -1
      (:balance (first accounts-by-id)))))

(defn account [id]
  (let [accounts (:accounts @registry)]
    (filter (fn [account] (= id (:id account))) accounts)))

(defn operation [id]
  (let [operations (:ledger @registry)]
    (filter (fn [operation] (= id (:id operation))) operations)))

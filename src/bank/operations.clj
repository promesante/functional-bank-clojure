(ns bank.operations
  (:require [bank.actions :as a]))

(defn deposit [accounts id amount]
  (mapv
   (fn [account]
     (if (= id (:id account))
       (update account :balance (partial a/deposit amount))
       account))
   accounts))

(defn withdraw [accounts id amount]
  (mapv
   (fn [account]
     (if (= id (:id account))
       (update account :balance (partial a/withdraw amount))
       account))
   accounts))

(defn set-state [accounts id state]
  (mapv
   (fn [account]
     (if (= id (:id account))
       (update account :state (constantly state))
       account))
   accounts))

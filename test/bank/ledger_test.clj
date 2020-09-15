(ns bank.ledger-test
  (:require [clojure.test :refer :all]
            [bank.ledger :refer :all]))

(def ledger
  [{:id 0 :type :create :account-id 0}
   {:id 1 :type :create :account-id 1}
   {:id 2 :type :create :account-id 2}
   {:id 3 :type :deposit :account-id 0 :amount 200}
   {:id 4 :type :deposit :account-id 1 :amount 400}
   {:id 5 :type :deposit :account-id 2 :amount 600}
   {:id 6 :type :withdraw :account-id 0 :amount 100}
   {:id 7 :type :withdraw :account-id 1 :amount 200}
   {:id 8 :type :transfer :debit-id 1 :credit-id 2 :amount 200}
   {:id 9 :type :delete :account-id 1}
   ])

(def ledger-2
  [{:id 0 :type :create :account-id 0}
   {:id 1 :type :create :account-id 1}
   {:id 2 :type :create :account-id 2}
   {:id 3 :type :deposit :account-id 0 :amount 200}
   {:id 4 :type :deposit :account-id 1 :amount 400}
   {:id 5 :type :deposit :account-id 2 :amount 600}
   {:id 6 :type :withdraw :account-id 0 :amount 100}
   {:id 7 :type :withdraw :account-id 1 :amount 200}
   {:id 8 :type :transfer :debit-id 1 :credit-id 2 :amount 200}
   {:id 9 :type :delete :account-id 1}
   {:id 10 :type :deposit :account-id 0 :amount 200}
   ])

(deftest can-record-account-or-transaction
  (testing "account / transaction"
    (is (= ledger-2 (transaction ledger :deposit 0 200)))
    ))

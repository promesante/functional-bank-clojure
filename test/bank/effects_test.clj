(ns bank.effects-test
  (:require [clojure.test :refer :all]
            [mount.core :as mount]
            [bank.effects]
            ))

(defn init []
  (ref
   {:accounts
    [{:id 0 :curr :us :balance 0 :state :inactive}
     {:id 1 :curr :eu :balance 200 :state :active}
     {:id 2 :curr :us :balance 700 :state :active}
    ]
    :ledger
    [{:id 0 :type :create :account-id 0}
     {:id 1 :type :create :account-id 1}
     {:id 2 :type :create :account-id 2}
     {:id 3 :type :deposit :account-id 0 :amount 200}
     {:id 4 :type :deposit :account-id 1 :amount 400}
     {:id 5 :type :deposit :account-id 2 :amount 600}
     {:id 6 :type :withdraw :account-id 0 :amount 100}
     {:id 7 :type :withdraw :account-id 1 :amount 200}
     {:id 8 :type :transfer :debit-id 0 :credit-id 2 :amount 100}
     {:id 9 :type :delete :account-id 0}
     ]}
   )
  )

(def registry {:start #(init)})

;; Transactions

(deftest can-make-deposit-into-account
  (testing "deposit"
    (let [_ (mount/start-with-states {#'bank.effects/registry registry})
          result (bank.effects/deposit 1 100)
          balance (bank.effects/balance 1)
          operation (first (bank.effects/operation 10))
          {:keys [id type account-id amount]} operation
          ]
      (is (= "success" result))
      (is (= 300 balance))
      (is (= 10 id))
      (is (= :deposit type))
      (is (= 1 account-id))
      (is (= 100 amount))
    )))

(deftest can-make-withdraw-from-account
  (testing "withdraw"
    (let [_ (mount/start-with-states {#'bank.effects/registry registry})
          result (bank.effects/withdraw 1 100)
          balance (bank.effects/balance 1)
          operation (first (bank.effects/operation 10))
          {:keys [id type account-id amount]} operation
          ]
      (is (= "success" result))
      (is (= 100 balance))
      (is (= 10 id))
      (is (= :withdraw type))
      (is (= 1 account-id))
      (is (= 100 amount))
    )))

(deftest can-make-transfer
  (testing "transfer"
    (let [_ (mount/start-with-states {#'bank.effects/registry registry})
          result (bank.effects/transfer 2 1 100)
          balance-debit (bank.effects/balance 2)
          balance-credit (bank.effects/balance 1)
          operation (first (bank.effects/operation 10))
          {:keys [id type debit-id credit-id amount]} operation
          ]
      (is (= "success" result))
      (is (= 600 balance-debit))
      (is (= 300 balance-credit))
      (is (= 10 id))
      (is (= :transfer type))
      (is (= 2 debit-id))
      (is (= 1 credit-id))
      (is (= 100 amount))
    )))

;; Account Creation and Deletion

(deftest can-create-account
  (testing "create account"
    (let [_ (mount/start-with-states {#'bank.effects/registry registry})
          result (bank.effects/account-create :eu)
          account (first (bank.effects/account 3))
          {:keys [curr balance state]} account
          operation (first (bank.effects/operation 10))
          {:keys [id type account-id]} operation
          ]
      (is (= "success" result))
      (is (= :eu curr))
      (is (= 0 balance))
      (is (= :active state))
      (is (= 10 id))
      (is (= :create type))
      (is (= 3 account-id))
    )))

(deftest can-delete-account
  (testing "delete account"
    (let [_ (mount/start-with-states {#'bank.effects/registry registry})
          result (bank.effects/account-delete 1)
          account (first (bank.effects/account 1))
          {:keys [curr balance state]} account
          operation (first (bank.effects/operation 10))
          {:keys [id type account-id]} operation
          ]
      (is (= "success" result))
      (is (= :eu curr))
      (is (= 200 balance))
      (is (= :inactive state))
      (is (= 10 id))
      (is (= :delete type))
      (is (= 1 account-id))
    )))

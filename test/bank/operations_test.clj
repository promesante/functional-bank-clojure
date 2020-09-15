(ns bank.operations-test
  (:require [clojure.test :refer :all]
            [bank.operations :refer :all]))

(def accounts
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 200 :state :active}
   {:id 2 :curr :us :balance 300 :state :active}])

(def accounts-1
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 300 :state :active}
   {:id 2 :curr :us :balance 300 :state :active}])

(def accounts-2
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 100 :state :active}
   {:id 2 :curr :us :balance 300 :state :active}])

(def accounts-3
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 0 :state :active}
   {:id 2 :curr :us :balance 300 :state :active}])

(def accounts-4
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 200 :state :active}
   {:id 2 :curr :us :balance 300 :state :active}])

(def accounts-5
  [{:id 0 :curr :us :balance 100 :state :active}
   {:id 1 :curr :eu :balance 200 :state :inactive}
   {:id 2 :curr :us :balance 300 :state :active}])

(deftest can-make-deposit-and-withdrawal-from-account
  (testing "deposit / withdraw"
    (is (= accounts-1 (deposit accounts 1 100)))
    (is (= accounts-2 (withdraw accounts 1 100)))
    (is (= accounts-3 (withdraw accounts 1 200)))
    (is (= accounts-4 (withdraw accounts 1 300)))
    (is (= accounts-5 (set-state accounts 1 :inactive)))
    ))

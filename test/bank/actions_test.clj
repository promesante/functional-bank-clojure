(ns bank.actions-test
  (:require [clojure.test :refer :all]
            [bank.actions :refer :all]))

(deftest can-make-deposit-and-withdrawal-from-account
  (testing "deposit / withdraw"
    (is (= 100 (withdraw 200 100)))
    (is (= 0 (withdraw 100 100)))
    (is (= 100 (withdraw 100 200)))
    (is (= 200 (deposit 100 100)))))

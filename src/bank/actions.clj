(ns bank.actions)

(defn withdraw [amount balance]
  (if (< balance amount)
    balance
    (- balance amount)
    ))

(defn deposit [amount balance]
  (+ balance amount))

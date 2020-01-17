(ns data-lake.main-test
  (:require [clojure.test :refer :all]
            [data-lake.main :refer :all]))

(deftest -main-arg-test
  (testing "empty args"
    (is (= (with-out-str (apply -main []))
           help-msg))))

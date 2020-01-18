(ns data-lake.main-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [data-lake.main :refer :all]
            [data-lake.cli :refer :all]
            [data-lake.core.sqlite :as sqlite]
            [data-lake.consts :refer [history-db-path]]
            ))

(deftest -main-arg-test
  (testing "args"
    (is (= (with-out-str (-main)) help-msg))
    (is (= (with-out-str (-main "help")) help-msg)))
  (let [pre-init (initiated?)
        history  (io/as-file history-db-path)]
    (when-not (initiated?)
      (println "Run `$lake init` first FOR TESTING SQLITE")
      (println)
      (println "Test for init. It will be reverted (history will be deleted after test):")
      (testing "init create history db and insert init cmd"
        (let [db    (sqlite/db-spec history-db-path)
              _     (-main "init")
              query (-> (h/select :cmd) 
                        (h/from :history)
                        sql/format)]
          (is (.exists history))
          (is (= "init" 
                 (-> (jdbc/query db query) first :cmd))))))
    (testing "already initiated"
      (is (= (with-out-str (-main "init"))
             no-need-init-msg)))
    (testing "sqlite create!"
      (let [db-path  "./test/fixture/test6.db"
            file     (io/as-file db-path)
            edn-path "./DB/sqlite/szmc_schema_0.1.0.edn"]
        (-main "sqlite" "new" db-path edn-path :no-log)
        (is (.exists file)) 
        (.delete file)))
    (testing "no-log"
      (let [db-path  "./test/fixture/test_no_log.db"
            db-file  (io/as-file db-path)
            history  (io/as-file history-db-path)
            edn-path "./DB/sqlite/cmd_history_0.1.0.edn"]
        (-main "sqlite" "new" db-path edn-path :no-log)
        (is (.exists db-file))
            ; TODO: check history isn't logged.
            ; If there is no history db then skip.
            ; If there is history then check no logging.
        (.delete db-file)))
    (when-not pre-init 
      (println "Remove created history db for testing") 
      (.delete history))))

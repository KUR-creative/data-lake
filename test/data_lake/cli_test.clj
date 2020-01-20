(ns data-lake.cli-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [data-lake.consts :as c]
            [data-lake.core.sqlite :as sqlite]
            [data-lake.cli :as cli]
            [data-lake.task.common :as tc]
            [data-lake.task.init :as init]
            [data-lake.task.help :as help]))

(deftest cli-run-args-test
  (testing "args"
    (is (= (with-out-str (cli/run)) help/help-msg))
    (is (= (with-out-str (cli/run "help")) help/help-msg)))
  (let [pre-init (tc/initiated?)
        history  (io/as-file c/history-db-path)]
    (when-not (tc/initiated?)
      (println "Run `$lake init` first FOR TESTING SQLITE")
      (println)
      (println "Test for init. It will be reverted (history will be deleted after test):")
      (testing "init create history db and insert init cmd"
        (let [db    (sqlite/db-spec c/history-db-path)
              _     (cli/run "init")
              query (-> (h/select :cmd) 
                        (h/from :history)
                        sql/format)]
          (is (.exists history))
          (is (= "init" 
                 (-> (jdbc/query db query) first :cmd))))))

    (testing "already initiated"
      (is (= (with-out-str (cli/run "init"))
             init/no-need-init-msg)))

    (testing "sqlite create!"
      (let [db-path  "./test/fixture/test6.db"
            file     (io/as-file db-path)
            edn-path "./DB/sqlite/szmc_schema_0.1.0.edn"]
        (cli/run "sqlite" "new" db-path edn-path :no-log)
        (is (.exists file)) 
        (.delete file)))

    (let [db-path    "./test/fixture/test_no_log.db"
          db-file    (io/as-file db-path)
          history-db (sqlite/db-spec c/history-db-path)
          edn-path   "./DB/sqlite/cmd_history_0.1.0.edn"
          query      (-> (h/select :cmd) 
                         (h/from :history)
                         sql/format)
          prev-n-cmd (count (jdbc/query history-db 
                                        query))]
      (testing "if :no-log then do not log cmd to history"
        (cli/run "sqlite" "new" db-path edn-path :no-log)
        (is (.exists db-file))
        (is (= prev-n-cmd 
               (count (jdbc/query history-db query))))
        (.delete db-file))

      (testing "if no :no-log then log cmd to history"
        (cli/run "sqlite" "new" db-path edn-path)
        (is (= (+ prev-n-cmd 1)
               (count (jdbc/query history-db query))))
        (.delete db-file)))

    (when-not pre-init 
      (println "Remove created history db for testing") 
      (.delete history))))

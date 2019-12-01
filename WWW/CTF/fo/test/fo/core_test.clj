(ns fo.core-test
  (:require [clojure.test :refer :all]
            [fo.core :refer :all]
            [fo.scheme :refer [fo-enc fo-dec]]))

(def source "123")
(def expected "至豆友智三量濟僧舍伊千阿北灭兄念开親牟")

(deftest fo-test
  (testing "enc and dec"
    (is (= (fo-enc source) (vec expected))
        (= (fo-dec expected) source))))

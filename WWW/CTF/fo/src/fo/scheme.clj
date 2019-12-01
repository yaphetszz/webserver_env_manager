(ns fo.scheme
  (:require [clojure.java.io :as jio]
            [taoensso.timbre :refer [warn]]
            [buddy.core.crypto :as crypto]
            [buddy.core.padding :as padding]
            [buddy.core.codecs :as codecs]
            [buddy.core.nonce :as nonce])
  )

(def fochars (list
            "謹" "穆" "僧" "室" "藝" "瑟" "彌" "提" "蘇" "醯" "盧" "呼" "舍" "參" "沙" "伊"
            "隸" "麼" "遮" "闍" "度" "蒙" "孕" "薩" "夷" "他" "姪" "豆" "特" "逝" "輸" "楞"
            "栗" "寫" "數" "曳" "諦" "羅" "故" "實" "訶" "知" "三" "藐" "耨" "依" "槃" "涅"
            "竟" "究" "想" "夢" "倒" "顛" "遠" "怖" "恐" "礙" "以" "亦" "智" "盡" "老" "至"
            "吼" "足" "幽" "王" "告" "须" "弥" "灯" "护" "金" "刚" "游" "戏" "宝" "胜" "通"
            "药" "师" "琉" "璃" "普" "功" "德" "山" "善" "住" "过" "去" "七" "未" "来" "贤"
            "劫" "千" "五" "百" "万" "花" "亿" "定" "六" "方" "名" "号" "东" "月" "殿" "妙"
            "尊" "树" "根" "西" "皂" "焰" "北" "清" "数" "精" "进" "首" "下" "寂" "量" "诸"
            "多" "释" "迦" "牟" "尼" "勒" "阿" "閦" "陀" "中" "央" "众" "生" "在" "界" "者"
            "行" "于" "及" "虚" "空" "慈" "忧" "各" "令" "安" "稳" "休" "息" "昼" "夜" "修"
            "持" "心" "求" "诵" "此" "经" "能" "灭" "死" "消" "除" "毒" "害" "高" "开" "文"
            "殊" "利" "凉" "如" "念" "即" "说" "曰" "帝" "毘" "真" "陵" "乾" "梭" "哈" "敬"
            "禮" "奉" "祖" "先" "孝" "雙" "親" "守" "重" "師" "愛" "兄" "弟" "信" "朋" "友"
            "睦" "宗" "族" "和" "鄉" "夫" "婦" "教" "孫" "時" "便" "廣" "積" "陰" "難" "濟"
            "急" "恤" "孤" "憐" "貧" "創" "廟" "宇" "印" "造" "經" "捨" "藥" "施" "茶" "戒"
            "殺" "放" "橋" "路" "矜" "寡" "拔" "困" "粟" "惜" "福" "排" "解" "紛" "捐" "資"
            ))
(def fochar->index (zipmap fochars (range)))
(def test-iv "00000166a524fd31d9af5aa2")
(def test-key "00000166a524fd31c6739e01fc255557db713f1e3bdd8d8b4a80bdee7ab586d7")

;; hex to byte
(def iv (codecs/hex->bytes (or (-> "FO_IV" System/getenv) test-iv)))
(def key32 (codecs/hex->bytes (or (-> "FO_KEY" System/getenv) test-key)))

;; try initialize once to verify iv/key
(crypto/encrypt (codecs/str->bytes "somedata") key32 iv {:algorithm :aes256-gcm})

(defmacro safe-op
  [& body]
  `(try ~@body
        (catch Exception e#
          (warn "caught exception:" (.getMessage e#))
          "佛不知道你在说什么")))

(defn foe
  ;; means fo-encoding: from byte-array to fo-encoded string
  [i]
  (let [byte-vec (vec i)
        ;; convert from -128 to 128, to 0-255(as index)
        ;; https://crossclj.info/ns/primitive-math/0.1.6/primitive-math.html#_byte-%3Eubyte
        unsigned-vec (map #(->> % long (bit-and 0xFF) short) byte-vec)

        ;; for random access
        v (vec unsigned-vec)]

    (mapcat #(nth fochars %) v)))

(defn fo-enc
  ;; encrypt, fo-encoding
  [i]
  (safe-op
   (let [data (codecs/str->bytes i)
         raw (crypto/encrypt data key32 iv {:algorithm :aes256-gcm})
         rsp (-> raw foe)]
     rsp)))

(defn fo-dec
  ;; fo-decoding, decrypt
  [i]
  (safe-op
   (let [indexes (map #(unchecked-byte (get fochar->index (str %))) (vec i))
         raw (crypto/decrypt (byte-array indexes) key32 iv {:algorithm :aes256-gcm})]
     (codecs/bytes->str raw))))

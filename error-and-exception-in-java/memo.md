# Chap1
## Javaの思想
- 不正な形式のコードは動かない
    - badly formed code will not be run.
- 公式なエラーを返す唯一の手段は exceptions
- コンパイラはこれに対応する処理の実装を強制する

## 例外とは
- exceptional conditionとは
    - 現在の処理で、コードが正常な動作を行えなくなる状態

## Javaではどう書くか

### 発生のさせ方
```java
if (t == null)
    throw new NullPointerException();
```
- ヒープ上に new を使って生成される
    - メモ:スコープ外にも伝搬する
- 現在の実行パスを停止し、例外ハンドラに処理を委譲
- exeption classの名前は何がうまくいかなかったかを表す

```java
throw  new NullPointerException("t == null")
```
- コンストラクタは2種
    - 引数なし
    - string

### 処理の仕方
```java
try {
   // 例外を起こす処理

} catch (Type1 ex1) {
    // Type1型の例外が起きた時の処理
} catch (Type2 ex2) {
    // Type1型の例外が起きた時の処理
}
```

- try句、catch句で処理を書く
- 例外に対応した後の処理のモデル
    - Resumption
        - 例外発生した処理に戻る
    - Termination
        - 例外発生した処理に戻らない
- JavaではTerminationを採用

- 再度throwすることができる

```java
catch (Exception e) {
    throw e;
}
```

- `Throwable`のサブクラスは例外を元に生成できる
```java
throw new RuntimeException(e);
```

### メソッドが発生させる例外の宣言
- `throws`キーワードでそのメソッドがthrowする例外の種類を宣言できる

```java
void f() throws TooBig, TooSmall, DivZero{

}
```

- コンパイラがこのメソッドの呼び出し元にこの例外の処理を強制できる
    - checked exceptionと呼ばれる
- 実際にはthrowする実装がなくても宣言してよい

### 独自の例外クラスの定義
継承により独自の例外クラスを定義できる

```java
class SimpleExecption extends Exception {}
```

- エラーメッセージの出力には`System.out`ではなく`System.err`を使う
- `printStackTrace()`で例外を発生させたメソッドまでのスタックトレースが生成できる

## Javaでの標準的な例外
### Throwableを持つクラス
Throwable
├── Error        ← JVMのエラー(usually not caught)
└── Exception    ← 実装者がハンドリングできるExceptions
    └── RuntimeException ← 非チェック例外(indicate bugs)

### 非チェック例外について
- コンパイラが実装時にハンドリングを強制しないException
- 自動的に発生させられる
    - NullPointerException
    - ArrayIndexOutOfBoundsException
- これらはバグと考えられるため、通常対応しない
- `RuntimeException`の理由
    - コントロール外の部分で起きたエラー
        - 例:Null reference
            - メモ:誰視点？どこまでが予想すべき責任範囲？
    - 実装者がチェックすべき内容
        - 例:配列のindexを超える
            - メモ:じゃあcheck例外でいいのでは
    - 追加調査
        - 前提:
            - 利用者
                - ソフトウェア、ライブラリの利用者
                - 実装をしていない
            - 実装者（programmer）
                - ソフトウェア、ライブラリの利用者
                - 実装している
        - checked例外
            - 使われる環境により起きる可能性がある
            - 利用者に対応してもらう必要がある or 対応してもらうことで回避可能なエラー
        - 非checked例外
            - 実行環境によらない
            - 実装者の実装によって回避できるもの（バグ）
        - まとめ
            - 利用者の対応が必要かいなかが点

### Check例外にまつわる問題
- 大規模な開発においては開発の障害になる
    - メモ:handlerの実装を強制するため
- しかし、以下のように例外を無視してはいけない

```java
try {
    // ...
} catch (ObligatoryException e) {} 
```
- 解消案1:コンソールに流す
    - 全部をmainまで伝搬させる
```java
public static void main(String[] args) throws Exception {
    // Let all exceptions go to the console
}
```
- 解消案2:非チェック例外に差し替える
    - 元の例外は`getCause()`で取得可能
    - `throws`宣言なしで取得できる
```java
try {

}catch (CheckedException e){
    throw new RuntimeException(e);
}
```
- メモ:例外の伝搬を面倒にしないようにするため？

## Exceptionの取り扱いガイドライン
Exceptionは以下のような方針で取り扱う。

### 正しいレベルで例外に対応する
- その抽象度レイヤーで自分がどうするべきか判断できない例外はキャッチしない
    - 何もできないのにcatchしてしまうとエラーを握りつぶしてしまう
- メモ:レベル->抽象度（技術->ビジネスロジック）の話？
    - 対応するロジックがその抽象レベルで考えるべきではない場合、上のレイヤーの処理に伝搬させる

```java
// 正しいレイヤーでキャッチしている例
// エラーが起きた場合はユーザーにメッセージを見せる
try {
    loadUserData(userId)
} catch (IOException e) {
    showMessageTo
    User("Could not load data. Please try again.");
}
```
- メモ:そのクラスでの責務と全体での分業度合いがわかってないと難しくない？

### 2. 問題を修正し、再度実行する
問題を修正し再実行できる場合は再度実行する
```java
int retries = 3;
while (retries > 0) {
    try {
        connectToServer();
        break; //成功したらループから出る
    }catch (ConnectionException e){
        retires--;
        waitOneSeconds();
    }
}
```

### 3. 再度実行せず、代わりの処理を行い処理を続ける
- 例外が起きた場合に代わりの処理を行い進めていい場合、retryせず進める

```java
String username;
try {
    username = loadUsername();
}
```

### 4. 再度実行せず、代わりの結果を返す

### 5. そのコンテキストで可能なことを全て行った後、上のレイヤーに同じ例外を返す

```java
catch (Exception e) {
    System.err.println("Err is happen!");
    throw e;
}
```

### 6. そのコンテキストで可能なことを全て行った後、上のレイヤーに異なる例外を返す

### 7. 実行を停止
JVM側のエラーなどは対応できないため、実行を停止

### 8.シンプルに 
- メソッド内でthrowするchecked例外を増やすとそのメソッドの利用を困難にする
- メモ:これ別の本でも言われてた（例外もinterfaceの一部）

### 9. 実装するライブラリとプログラムをsaferに
- セクションのまとめ

#### Java以前
- 例外は無視されやすかった
    - 例外を値で返す、フラグを変えるなどの実装
- 例外をなぜ無視してはいけないのか
    - プログラムが気づかず意図しない動作をしている可能性がある
        - null pointerの参照
    - 意図しない挙動をするぐらいなら止まったほうがいい という考え方

#### Java
- Checked例外というライブラリ実装者と利用者間の契約
    - どんな例外が起きそれに利用者のコンテキストでどのように対応すべきか判断してほしいかが明示的に書かれている
    - これへの対応をコンパイラが強制している
- RuntimeErrorでの実装ミスへの対応


# メモ
- 単語から関連する推察とその理由を引いてくる
    - ヒープ上に作成されるオブジェクト -> グローバルなもの -> 確かにスコープを超えて共有したいのであっている
- 具体例を用いて考える
- 例外についても普通の実装と同じようにコンテキストの分割を考える
    - 抽象度の分割
- あるものの存在意義は「それがなかった場合にどうなるか」を考えることでわかる
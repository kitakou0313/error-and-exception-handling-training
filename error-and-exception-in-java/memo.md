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
            - 利用者に対応してもらう必要がある
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

## ガイドライン

# メモ
- 単語から関連する推察とその理由を引いてくる
    - ヒープ上に作成されるオブジェクト -> グローバルなもの -> 確かにスコープを超えて共有したいのであっている
- 具体例を用いて考える
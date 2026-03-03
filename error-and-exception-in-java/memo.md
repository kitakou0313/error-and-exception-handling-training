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

# メモ
- 単語から関連する推察とその理由を引いてくる
    - ヒープ上に作成されるオブジェクト -> グローバルなもの -> 確かにスコープを超えて共有したいのであっている
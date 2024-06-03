RE Practice Version of [the Pokedex repo](https://github.com/skydoves/pokedex-compose) which has
some hidden 'features'.

## Prompt

This Pokèdex app has odd behavior shown to only specific users, but we can't find the code for it.

Find the hidden component, without looking at the sources :)

<details>
  <summary>Hint 1</summary>

  The app makes use of Hilt so most code is not 'directly' connected due to generated classes.
</details>
<details>
  <summary>Hint 2</summary>

  Avoid 'diving deep' into Compose from onCreate. It is tedious to do so due to R8 Lambda Groups. Instead, try searching for suspicious strings like "Base64"
  
</details>
<details>
  <summary>Hint 3</summary>

  The app performs Dynamic Code Loading (DCL). The DCL payload is encrypted. How can we decrypt it? Where is this code?
  
</details>

## Disclaimer

The contents in this repo are for educational purposes only. The "CTF" flag contains no malicious
code and cannot cause harm assuming this repo is not compromised.

Having said that, please take all safety precautions you would as with any other malicious
samples.

Thanks!

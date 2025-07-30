# 🔥 Firebase Kurulum Rehberi

Bu rehber, Clash of Words uygulaması için Firebase projesini nasıl kuracağınızı açıklar.

## 📋 Gereksinimler

- Google hesabı
- Firebase Console erişimi
- Android Studio

## 🚀 Adım Adım Kurulum

### 1. Firebase Projesi Oluşturma

1. [Firebase Console](https://console.firebase.google.com/)'a gidin
2. "Proje Ekle" butonuna tıklayın
3. Proje adını girin: `Clash of Words`
4. Google Analytics'i etkinleştirin (isteğe bağlı)
5. "Proje Oluştur" butonuna tıklayın

### 2. Android Uygulaması Ekleme

1. Firebase projenizde "Android" simgesine tıklayın
2. Android paket adını girin: `com.furkan.clashofwords`
3. Uygulama takma adını girin: `Clash of Words`
4. "Uygulama Kaydet" butonuna tıklayın

### 3. google-services.json Dosyasını İndirme

1. `google-services.json` dosyasını indirin
2. Bu dosyayı projenizin `app/` klasörüne yerleştirin
3. Dosyanın `.gitignore`'da olduğundan emin olun (güvenlik için)

### 4. Firebase Servislerini Etkinleştirme

#### Authentication
1. Sol menüden "Authentication" seçin
2. "Başlayın" butonuna tıklayın
3. "Sign-in method" sekmesine gidin
4. "Anonymous" sağlayıcısını etkinleştirin
5. "Kaydet" butonuna tıklayın

#### Firestore Database
1. Sol menüden "Firestore Database" seçin
2. "Veritabanı oluştur" butonuna tıklayın
3. Güvenlik modunu seçin:
   - **Test modu** (geliştirme için)
   - **Production mode** (canlı için)
4. Veritabanı konumunu seçin (Türkiye için `europe-west1` önerilir)

#### Storage
1. Sol menüden "Storage" seçin
2. "Başlayın" butonuna tıklayın
3. Güvenlik kurallarını seçin:
   - **Test modu** (geliştirme için)
   - **Production mode** (canlı için)

### 5. Güvenlik Kuralları

#### Firestore Güvenlik Kuralları
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Kullanıcılar sadece kendi verilerini okuyabilir/yazabilir
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Oyun verileri için genel okuma izni
    match /games/{gameId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

#### Storage Güvenlik Kuralları
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Kullanıcılar sadece kendi profil resimlerini yükleyebilir
    match /profile-pictures/{userId}/{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 6. Android Studio'da Yapılandırma

1. Android Studio'da projeyi açın
2. Gradle senkronizasyonunu bekleyin
3. `google-services.json` dosyasının `app/` klasöründe olduğunu doğrulayın
4. Projeyi derleyin ve çalıştırın

## 🔧 Sorun Giderme

### Yaygın Hatalar

#### "google-services.json not found"
- Dosyanın `app/` klasöründe olduğunu kontrol edin
- Gradle senkronizasyonunu yeniden çalıştırın

#### "Firebase App not initialized"
- `google-services.json` dosyasının doğru olduğunu kontrol edin
- Firebase Console'da uygulamanın doğru kaydedildiğini doğrulayın

#### "Permission denied"
- Firestore ve Storage güvenlik kurallarını kontrol edin
- Authentication'ın etkin olduğunu doğrulayın

## 📱 Test Etme

1. Uygulamayı çalıştırın
2. "Misafir Giriş" butonuna tıklayın
3. Ana ekrana yönlendirildiğinizi doğrulayın
4. Firebase Console'da Authentication bölümünde yeni kullanıcıyı görün

## 🔒 Güvenlik Notları

- `google-services.json` dosyasını asla GitHub'a yüklemeyin
- Production'da güvenlik kurallarını sıkılaştırın
- API anahtarlarını güvenli tutun
- Düzenli olarak güvenlik denetimi yapın

## 📞 Destek

Sorun yaşarsanız:
1. Firebase Console'da hata mesajlarını kontrol edin
2. Android Studio logcat'ini inceleyin
3. Firebase dokümantasyonunu okuyun
4. GitHub Issues'da sorun bildirin

---

Bu rehberi takip ederek Firebase'i başarıyla kurabilirsiniz! 🎉 
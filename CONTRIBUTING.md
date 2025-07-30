# 🤝 Katkıda Bulunma Rehberi

Clash of Words projesine katkıda bulunmak istediğiniz için teşekkürler! Bu rehber, projeye nasıl katkıda bulunabileceğinizi açıklar.

## 📋 Katkı Türleri

- 🐛 **Hata Düzeltmeleri**: Kod hatalarını düzeltme
- ✨ **Yeni Özellikler**: Yeni oyun özellikleri ekleme
- 📚 **Dokümantasyon**: README, kod yorumları güncelleme
- 🎨 **UI/UX İyileştirmeleri**: Arayüz ve kullanıcı deneyimi geliştirme
- 🧪 **Testler**: Unit testler ve UI testler ekleme
- 🌍 **Çeviri**: Yeni dil desteği ekleme

## 🚀 Başlarken

### Gereksinimler
- Android Studio Arctic Fox veya üzeri
- Java 11
- Git
- Firebase hesabı (geliştirme için)

### Kurulum
1. Bu repository'yi fork edin
2. Yerel makinenizde klonlayın:
   ```bash
   git clone https://github.com/YOUR_USERNAME/clash-of-words.git
   cd clash-of-words
   ```
3. Firebase kurulumunu tamamlayın (bkz. [FIREBASE_SETUP.md](FIREBASE_SETUP.md))
4. Projeyi Android Studio'da açın

## 🔧 Geliştirme Süreci

### 1. Branch Oluşturma
```bash
git checkout -b feature/your-feature-name
# veya
git checkout -b fix/your-bug-fix
```

### 2. Geliştirme
- Kodunuzu yazın
- Mevcut kod stilini takip edin
- Türkçe yorumlar kullanın
- Unit testler ekleyin (mümkünse)

### 3. Commit Mesajları
Commit mesajlarınızı açık ve anlaşılır yazın:
```bash
git commit -m "feat: yeni oyun modu eklendi"
git commit -m "fix: profil resmi yükleme hatası düzeltildi"
git commit -m "docs: README güncellendi"
```

### 4. Push ve Pull Request
```bash
git push origin feature/your-feature-name
```

## 📝 Kod Stili

### Java
- **Naming Conventions**: camelCase değişkenler, PascalCase sınıflar
- **Indentation**: 4 boşluk
- **Comments**: Türkçe yorumlar kullanın
- **Imports**: Gereksiz import'ları temizleyin

### XML Layouts
- **Naming**: snake_case
- **Indentation**: 2 boşluk
- **Attributes**: Mantıklı sıralama

### Örnek Kod
```java
/**
 * Kullanıcının oyun skorunu hesaplar
 * @param correctAnswers Doğru cevap sayısı
 * @param timeBonus Zaman bonusu
 * @return Toplam skor
 */
public int calculateScore(int correctAnswers, int timeBonus) {
    int baseScore = correctAnswers * 10;
    return baseScore + timeBonus;
}
```

## 🧪 Test Etme

### Unit Tests
- `src/test/` klasöründe testlerinizi yazın
- Test coverage'ı artırmaya çalışın
- Mock kullanarak Firebase bağımlılıklarını test edin

### UI Tests
- `src/androidTest/` klasöründe UI testlerinizi yazın
- Farklı ekran boyutlarını test edin
- Accessibility testleri ekleyin

## 📋 Pull Request Süreci

### 1. PR Açmadan Önce
- [ ] Kodunuzu test edin
- [ ] Lint hatalarını düzeltin
- [ ] Commit mesajlarınızı kontrol edin
- [ ] README'yi güncelleyin (gerekirse)

### 2. PR Açarken
- Açıklayıcı başlık kullanın
- Değişiklikleri detaylandırın
- Screenshot ekleyin (UI değişiklikleri için)
- İlgili issue'yu referans gösterin

### 3. PR Template
```markdown
## Değişiklik Türü
- [ ] Bug fix
- [ ] Yeni özellik
- [ ] Dokümantasyon
- [ ] Test

## Açıklama
Bu PR ne yapıyor?

## Test Edildi
- [ ] Android Studio'da derlendi
- [ ] Emülatörde test edildi
- [ ] Unit testler geçti

## Screenshots (varsa)
```

## 🐛 Hata Bildirimi

### Issue Açarken
- Açıklayıcı başlık kullanın
- Adım adım reprodüksiyon adımları yazın
- Beklenen ve gerçek davranışı açıklayın
- Cihaz bilgilerini ekleyin
- Screenshot/video ekleyin

### Issue Template
```markdown
## Hata Açıklaması
Kısa ve net açıklama

## Reprodüksiyon Adımları
1. Uygulamayı aç
2. ...
3. Hata oluşur

## Beklenen Davranış
Ne olması gerekiyordu?

## Gerçek Davranış
Ne oldu?

## Cihaz Bilgileri
- Android Version: 
- Cihaz Modeli: 
- Uygulama Versiyonu: 

## Screenshots/Videos
```

## 🎯 Katkı Alanları

### Öncelikli Konular
- [ ] Google Sign-In entegrasyonu
- [ ] Çoklu dil desteği
- [ ] Offline mod
- [ ] Push notifications
- [ ] Sosyal medya paylaşımı
- [ ] Oyun istatistikleri
- [ ] Tema desteği (Dark/Light mode)

### Yeni Özellik Önerileri
- [ ] Yeni oyun modları
- [ ] Özel soru kategorileri
- [ ] Arkadaş sistemi geliştirmeleri
- [ ] Mağaza öğeleri
- [ ] Başarım sistemi

## 📞 İletişim

- **GitHub Issues**: Hata bildirimi ve özellik önerileri
- **Discussions**: Genel tartışmalar
- **Email**: [your-email@example.com]

## 🙏 Teşekkürler

Katkıda bulunan herkese teşekkürler! Projeyi daha iyi hale getirmek için çalışıyoruz.

---

Bu rehberi takip ederek projeye katkıda bulunabilirsiniz! 🚀 
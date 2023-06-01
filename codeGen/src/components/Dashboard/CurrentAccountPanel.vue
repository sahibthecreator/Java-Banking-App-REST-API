<template>
    <div class="accountPage" v-if="currentAccount">
        <div class="topPart">
            <b-button variant="black" @click="returnDashboardView()">Return to dashboard</b-button>
            <div class="cardPanel">
                <h1>{{ currentAccount.typeOfAccount.charAt(0) + currentAccount.typeOfAccount.toLowerCase().slice(1) }}
                    Account
                </h1>
                <div class="flip-card">
                    <div class="flip-card-inner">
                        <div class="flip-card-front">
                            <p class="heading_8264">WAVR BANKING</p>
                            <svg viewBox="0 0 48 48" height="36" width="36" y="0px" x="0px"
                                xmlns="http://www.w3.org/2000/svg" class="logo">
                                <path d="M32 10A14 14 0 1 0 32 38A14 14 0 1 0 32 10Z" fill="#ff9800"></path>
                                <path d="M16 10A14 14 0 1 0 16 38A14 14 0 1 0 16 10Z" fill="#d50000"></path>
                                <path
                                    d="M18,24c0,4.755,2.376,8.95,6,11.48c3.624-2.53,6-6.725,6-11.48s-2.376-8.95-6-11.48 C20.376,15.05,18,19.245,18,24z"
                                    fill="#ff3d00"></path>
                            </svg>
                            <svg xml:space="preserve" viewBox="0 0 50 50" height="30px" width="30px" y="0px" x="0px"
                                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg" id="Layer_1"
                                class="chip" version="1.1">
                                <image href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAMAAAAp4XiDAAAABGdBTUEAALGPC/xhBQAAACBjSFJN
              AAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAB6VBMVEUAAACNcTiVeUKVeUOY
              fEaafEeUeUSYfEWZfEaykleyklaXe0SWekSZZjOYfEWYe0WXfUWXe0WcgEicfkiXe0SVekSXekSW
              ekKYe0a9nF67m12ZfUWUeEaXfESVekOdgEmVeUWWekSniU+VeUKVeUOrjFKYfEWliE6WeESZe0GS
              e0WYfES7ml2Xe0WXeESUeEOWfEWcf0eWfESXe0SXfEWYekSVeUKXfEWxklawkVaZfEWWekOUekOW
              ekSYfESZe0eXekWYfEWZe0WZe0eVeUSWeETAnmDCoWLJpmbxy4P1zoXwyoLIpWbjvXjivnjgu3bf
              u3beunWvkFWxkle/nmDivXiWekTnwXvkwHrCoWOuj1SXe0TEo2TDo2PlwHratnKZfEbQrWvPrWua
              fUfbt3PJp2agg0v0zYX0zYSfgkvKp2frxX7mwHrlv3rsxn/yzIPgvHfduXWXe0XuyIDzzISsjVO1
              lVm0lFitjVPzzIPqxX7duna0lVncuHTLqGjvyIHeuXXxyYGZfUayk1iyk1e2lln1zYTEomO2llrb
              tnOafkjFpGSbfkfZtXLhvHfkv3nqxH3mwXujhU3KqWizlFilh06khk2fgkqsjlPHpWXJp2erjVOh
              g0yWe0SliE+XekShhEvAn2D///+gx8TWAAAARnRSTlMACVCTtsRl7Pv7+vxkBab7pZv5+ZlL/UnU
              /f3SJCVe+Fx39naA9/75XSMh0/3SSkia+pil/KRj7Pr662JPkrbP7OLQ0JFOijI1MwAAAAFiS0dE
              orDd34wAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfnAg0IDx2lsiuJAAACLElEQVRIx2Ng
              GAXkAUYmZhZWPICFmYkRVQcbOwenmzse4MbFzc6DpIGXj8PD04sA8PbhF+CFaxEU8iWkAQT8hEVg
              OkTF/InR4eUVICYO1SIhCRMLDAoKDvFDVhUaEhwUFAjjSUlDdMiEhcOEItzdI6OiYxA6YqODIt3d
              I2DcuDBZsBY5eVTr4xMSYcyk5BRUOXkFsBZFJTQnp6alQxgZmVloUkrKYC0qqmji2WE5EEZuWB6a
              lKoKdi35YQUQRkFYPpFaCouKIYzi6EDitJSUlsGY5RWVRGjJLyxNy4ZxqtIqqvOxaVELQwZFZdkI
              JVU1RSiSalAt6rUwUBdWG1CP6pT6gNqwOrgCdQyHNYR5YQFhDXj8MiK1IAeyN6aORiyBjByVTc0F
              qBoKWpqwRCVSgilOaY2OaUPw29qjOzqLvTAchpos47u6EZyYnngUSRwpuTe6D+6qaFQdOPNLRzOM
              1dzhRZyW+CZouHk3dWLXglFcFIflQhj9YWjJGlZcaKAVSvjyPrRQ0oQVKDAQHlYFYUwIm4gqExGm
              BSkutaVQJeomwViTJqPK6OhCy2Q9sQBk8cY0DxjTJw0lAQWK6cOKfgNhpKK7ZMpUeF3jPa28BCET
              amiEqJKM+X1gxvWXpoUjVIVPnwErw71nmpgiqiQGBjNzbgs3j1nus+fMndc+Cwm0T52/oNR9lsdC
              S24ra7Tq1cbWjpXV3sHRCb1idXZ0sGdltXNxRateRwHRAACYHutzk/2I5QAAACV0RVh0ZGF0ZTpj
              cmVhdGUAMjAyMy0wMi0xM1QwODoxNToyOSswMDowMEUnN7UAAAAldEVYdGRhdGU6bW9kaWZ5ADIw
              MjMtMDItMTNUMDg6MTU6MjkrMDA6MDA0eo8JAAAAKHRFWHRkYXRlOnRpbWVzdGFtcAAyMDIzLTAy
              LTEzVDA4OjE1OjI5KzAwOjAwY2+u1gAAAABJRU5ErkJggg==" y="0" x="0" height="50" width="50" id="image0"></image>
                            </svg>
                            <svg xml:space="preserve" viewBox="0 0 50 50" height="20px" width="20px" y="0px" x="0px"
                                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg" id="Layer_1"
                                class="contactless" version="1.1">
                                <image href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAQAAAC0NkA6AAAABGdBTUEAALGPC/xhBQAAACBjSFJN
              AAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZ
              cwAACxMAAAsTAQCanBgAAAAHdElNRQfnAg0IEzgIwaKTAAADDklEQVRYw+1XS0iUURQ+f5qPyjQf
              lGRFEEFK76koKGxRbWyVVLSOgsCgwjZBJJYuKogSIoOonUK4q3U0WVBWFPZYiIE6kuArG3VGzK/F
              fPeMM/MLt99/NuHdfPd888/57jn3nvsQWWj/VcMlvMMd5KRTogqx9iCdIjUUmcGR9ImUYowyP3xN
              GQJoRLVaZ2DaZf8kyjEJALhI28ELioyiwC+Rc3QZwRYyO/DH51hQgWm6DMIh10KmD4u9O16K49it
              VoPOAmcGAWWOepXIRScAoJZ2Frro8oN+EyTT6lWkkg6msZfMSR35QTJmjU0g15tIGSJ08ZZMJkJk
              HpNZgSkyXosS13TkJpZ62mPIJvOSzC1bp8vRhhCakEk7G9/o4gmZdbpsTcKu0m63FbnBP9Qrc15z
              bkbemfgNDtEOI8NO5L5O9VYyRYgmJayZ9nPaxZrSjW4+F6Uw9yQqIiIZwhp2huQTf6OIvCZyGM6g
              DJBZbyXifJXr7FZjGXsdxADxI7HUJFB6iWvsIhFpkoiIiGTJfjJfiCuJg2ZEspq9EHGVpYgzKqwJ
              qSAOEwuJQ/pxPvE3cYltJCLdxBLiSKKIE5HxJKcTRNeadxfhDiuYw44zVs1dxKwRk/uCxIiQkxKB
              sSctRVAge9g1E15EHE6yRUaJecRxcWlukdRIbGFOSZCMWQA/iWauIP3slREHXPyliqBcrrD71Amz
              Z+rD1Mt2Yr8TZc/UR4/YtFnbijnHi3UrN9vKQ9rPaJf867ZiaqDB+czeKYmd3pNa6fuI75MiC0uX
              XSR5aEMf7s7a6r/PudVXkjFb/SsrCRfROk0Fx6+H1i9kkTGn/E1vEmt1m089fh+RKdQ5O+xNJPUi
              cUIjO0Dm7HwvErEr0YxeibL1StSh37STafE4I7zcBdRq1DiOkdmlTJVnkQTBTS7X1FYyvfO4piaI
              nKbDCDaT2anLudYXCRFsQBgAcIF2/Okwgvz5+Z4tsw118dzruvIvjhTB+HOuWy8UvovEH6beitBK
              xDyxm9MmISKCWrzB7bSlaqGlsf0FC0gMjzTg6GgAAAAldEVYdGRhdGU6Y3JlYXRlADIwMjMtMDIt
              MTNUMDg6MTk6NTYrMDA6MDCjlq7LAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDIzLTAyLTEzVDA4OjE5
              OjU2KzAwOjAw0ssWdwAAACh0RVh0ZGF0ZTp0aW1lc3RhbXAAMjAyMy0wMi0xM1QwODoxOTo1Nisw
              MDowMIXeN6gAAAAASUVORK5CYII=" y="0" x="0" height="50" width="50" id="image0"></image>
                            </svg>
                            <p class="number">{{ currentAccount.iban }}</p>
                            <p class="valid_thru">VALID THRU</p>
                            <p class="date_8264">1 2 / 2 4</p>
                            <p class="name">{{ user.firstName }} {{ user.lastName }}</p>
                        </div>
                        <div class="flip-card-back">
                            <div class="strip"></div>
                            <div class="mstrip"></div>
                            <div class="sstrip">
                                <p class="code">***</p>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <div class="cardPanel">
                <h1>Balance</h1>
                <h1><b>€ {{ currentAccount.balance }}</b></h1>
            </div>

            <div class="cardPanel">
                <h1>Details</h1>
                <p><b>IBAN: </b> {{ currentAccount.iban }}</p>
                <p><b>Date of openning: </b> {{ currentAccount.dateOfOpening }}</p>
                <p><b>Absolute limit: </b> {{ currentAccount.absoluteLimit }}</p>
                <p><b>Is active: </b> {{ currentAccount.active ? "Yes" : "No" }}</p>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: 'CurrentAccountPanel',
    props: {
        currentAccount: Object,
        user: Object,
    },
    methods: {
        returnDashboardView() {
            this.$emit('returnBack');
        }
    }
}
</script>

<style lang="scss" scoped>
.accountPage {
    .topPart {
        display: flex;
        justify-content: space-evenly;
    }
}

.cardPanel {
    background: white;
    padding: 20px;
    box-shadow: 0 0 30px #14141417;
    border-radius: 35px;
    width: min-content;
    min-width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    p {
        white-space: nowrap;
    }

    h1 {
        white-space: nowrap;
        margin-bottom: 10%;
    }

}


//credit card 
.flip-card {
    background-color: transparent;
    width: 240px;
    height: 154px;
    -webkit-perspective: 1000px;
    perspective: 1000px;
    color: white;
}

.heading_8264 {
    position: absolute;
    letter-spacing: .2em;
    font-size: 0.5em;
    top: 2em;
    left: 18.6em;
}

.logo {
    position: absolute;
    top: 6.8em;
    left: 11.7em;
}

.chip {
    position: absolute;
    top: 2.3em;
    left: 1.5em;
}

.contactless {
    position: absolute;
    top: 3.5em;
    left: 12.4em;
}

.number {
    position: absolute;
    font-weight: bold;
    font-size: .6em;
    top: 8.3em;
    left: 1.6em;
}

.valid_thru {
    position: absolute;
    font-weight: bold;
    top: 63%;
    font-size: 7px;
    left: 9%;
    color: white;
}

.date_8264 {
    position: absolute;
    font-weight: bold;
    font-size: 0.5em;
    top: 13.6em;
    left: 3.2em;
}

.name {
    position: absolute;
    font-weight: bold;
    font-size: 0.5em;
    top: 16.1em;
    left: 2em;
}

.strip {
    position: absolute;
    background-color: black;
    width: 15em;
    height: 1.5em;
    top: 2.4em;
    background: repeating-linear-gradient(45deg,
            #303030,
            #303030 10px,
            #202020 10px,
            #202020 20px);
}

.mstrip {
    position: absolute;
    background-color: rgb(255, 255, 255);
    width: 8em;
    height: 0.8em;
    top: 5em;
    left: .8em;
    border-radius: 2.5px;
}

.sstrip {
    position: absolute;
    background-color: rgb(255, 255, 255);
    width: 4.1em;
    height: 0.8em;
    top: 5em;
    left: 10em;
    border-radius: 2.5px;
}

.code {
    font-weight: bold;
    text-align: center;
    margin: .2em;
    color: black;
}

.flip-card-inner {
    position: relative;
    width: 100%;
    height: 100%;
    text-align: center;
    -webkit-transition: -webkit-transform 0.8s;
    transition: -webkit-transform 0.8s;
    transition: transform 0.8s;
    transition: transform 0.8s, -webkit-transform 0.8s;
    -webkit-transform-style: preserve-3d;
    transform-style: preserve-3d;
}

.flip-card:hover .flip-card-inner {
    -webkit-transform: rotateY(180deg);
    transform: rotateY(180deg);
}

.flip-card-front,
.flip-card-back {
    -webkit-box-shadow: 0 8px 14px 0 rgba(0, 0, 0, 0.2);
    box-shadow: 0 8px 14px 0 rgba(0, 0, 0, 0.2);
    position: absolute;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    -webkit-backface-visibility: hidden;
    backface-visibility: hidden;
    border-radius: 1rem;
}

.flip-card-front {
    -webkit-box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 2px, rgba(0, 0, 0, 0.3) 0px 7px 13px -3px, rgba(0, 0, 0, 0.2) 0px -1px 0px inset;
    box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 2px, rgba(0, 0, 0, 0.3) 0px 7px 13px -3px, rgba(0, 0, 0, 0.2) 0px -1px 0px inset;
    background-color: #000000;
}

.flip-card-back {
    -webkit-box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 2px, rgba(0, 0, 0, 0.3) 0px 7px 13px -3px, rgba(0, 0, 0, 0.2) 0px -1px 0px inset;
    box-shadow: rgba(0, 0, 0, 0.4) 0px 2px 2px, rgba(0, 0, 0, 0.3) 0px 7px 13px -3px, rgba(0, 0, 0, 0.2) 0px -1px 0px inset;
    background-color: #171717;
    -webkit-transform: rotateY(180deg);
    transform: rotateY(180deg);
}
</style>
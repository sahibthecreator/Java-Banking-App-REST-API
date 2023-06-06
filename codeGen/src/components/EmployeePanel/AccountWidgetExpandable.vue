<script setup>
import AccountIcon from '../Dashboard/AccountIcon.vue';
</script>

<template>
    <div class="account" v-if="!expanded" @click="expanded = true">
        <div class="d-flex info">
            <AccountIcon :accountName="user.firstName"></AccountIcon>
            <div class="title">
                <p class="my-auto ml-3">{{ account.typeOfAccount }} Account</p>
                <p class="my-auto ml-3 iban">{{ account.iban }}</p>
            </div>
            <p class="balance">€ {{ formatPrice(account.balance) }}</p>
        </div>
    </div>

    <div class="expanded account" v-else>
        <button type="button" class="dismiss" @click="expanded = false">×</button>
        <div class="info">
            <AccountIcon :accountName="user.firstName" class="icon"></AccountIcon>
            <div class="accountDetails">
                <div class="detailCard">
                    <label>Account ID</label>
                    <span type="text">{{ account.id }}</span>
                </div>
                <div class="detailCards">
                    <div class="detailCard">
                        <label>Account Holder</label>
                        <span type="text">{{ user.firstName + " " + user.lastName }}</span>
                    </div>
                    <div class="detailCard">
                        <label>IBAN</label>
                        <span type="text">{{ account.iban }}</span>
                    </div>
                </div>

                <div class="detailCard">
                    <label>Account</label>
                    <span type="text">{{ account.typeOfAccount }}</span>
                </div>
                <div class="detailCards">
                    <div class="detailCard">
                        <label>Balance</label>
                        <span type="text">€ {{ account.balance }}</span>
                    </div>
                    <div class="detailCard">
                        <label>Absolute Limit €</label>
                        <input type="text" v-model="account.absoluteLimit" @input="dataEdited = true" />
                    </div>
                </div>

                <div class="detailCards">
                    <div class="detailCard">
                        <label>Account</label>
                        <span type="text">{{ account.typeOfAccount }}</span>
                    </div>
                    <div class="detailCard">
                        <label>Date of opening</label>
                        <span type="text">{{ account.dateOfOpening }}</span>
                    </div>
                    <div class="detailCard">
                        <label>Account Active</label>
                        <span type="text">{{ account.active ? "Yes" : "No" }}</span>
                    </div>
                </div>
                <div class="detailCard">
                    <label>User ID</label>
                    <span type="text">{{ user.id }}</span>
                </div>
            </div>

            <div class="userControls">
                <button class="button btn-warning" @click="updateAccount()" v-if="dataEdited">Save</button>
                <button class="button btn-danger" @click="updateAccountStatus()">{{ account.active ? 'Deactivate' :
                    'Activate' }}</button>
            </div>
        </div>
    </div>

    <div class="card" v-if="statusPopupCardEnabled">
        <div class="header">
            <!-- Loading -->
            <div class="spinnerContainer" v-if="loading">
                <div class="spinner"></div>
                <div class="loader">
                    <div class="words">
                        <span class="word">Updating account</span>
                    </div>
                </div>
            </div>
            <!-- /Loading -->

            <div class="image" v-if="!loading && !error">
                <img src="@/assets/SuccessIcon.png" class="success icon" />
            </div>
            <div class="content" v-if="!loading">
                <span class="title">{{ statusTitle }}</span>
            </div>
            <div class="actions">
                <button type="button" class="history" @click="closePopup()" v-if="!loading">Close</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'AccountWidgetExpandable',
    props: {
        user: Object,
        account: Object,
    },
    data() {
        return {
            expanded: false,

            statusPopupCardEnabled: false,
            error: false,
            loading: false,
            delay: ms => new Promise(res => setTimeout(res, ms)),
            dataEdited: false,
            statusTitle: "",
        }
    },
    methods: {
        formatPrice(value) {
            let val = (value / 1).toFixed(2).replace('.', ',');
            return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        },
        async updateAccount() {
            let request = {
                iban: this.account.iban,
                accountData: this.account
            }
            try {
                await this.$store.dispatch('updateAccount', request);
                this.loading = true;
                this.statusPopupCardEnabled = true;
                this.statusTitle = `Account updated successfully`;
                await this.delay(500);
                this.loading = false;
            } catch (error) {
                this.error = true;
                this.statusTitle = error.message;
                this.loading = false;
                this.statusPopupCardEnabled = true;
            }

        },
        async updateAccountStatus() {
            try {
                switch (this.account.active) {
                    case true:
                        await this.$store.dispatch('deactivateAccount', this.account.iban);
                        this.statusTitle = `Account was deactivated`;
                        break;
                    case false:
                        await this.$store.dispatch('activateAccount', this.account.iban);
                        this.statusTitle = `Account was activated`;
                        break;
                }
                this.loading = true;
                this.statusPopupCardEnabled = true;
                await this.delay(500);
                this.loading = false;
            } catch (error) {
                this.error = true;
                this.statusTitle = error.message;
                this.loading = false;
                this.statusPopupCardEnabled = true;
            }
        },
        closePopup() {
            if (this.error)
                this.statusPopupCardEnabled = false;
            else
                document.location.reload();
        }
    },
};

var formatter = new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR',
});
</script>

<style lang="scss" scoped>
.account {
    height: fit-content;
    width: 95%;
    background-color: var(--white);
    padding: 15px 15px;
    border-radius: 15px;
    box-shadow: 0 0 30px #14141417;
    cursor: pointer;

    &:hover {
        filter: contrast(1.1);
    }

    .info {
        display: flex;
        flex-direction: row;
        gap: 20px;
        align-items: center;

        .title {
            p {
                font-weight: 500;
            }
        }

        .iban {
            color: var(--gray-light);
            font-size: 0.9rem;
        }

        .balance {
            margin-bottom: 0px;
            margin-left: auto;
            font-size: 1.2rem;
            font-weight: 00;
            text-align: left;
        }
    }

    &.expanded {
        .info {
            flex-direction: column;

            .accountDetails {
                width: 100%;
            }
        }

        .detailCards {
            display: flex;
            justify-content: space-between;
            gap: 3%;
            flex-grow: 1;
            width: 100%;

        }

        .userControls {
            margin: 3% 0 5% 0;
            width: fit-content;
            display: flex;
            flex-direction: row;
            justify-content: center;
            gap: 5%;
            width: 85%;

            button {
                height: fit-content;
                padding-block: 5px;
                width: 47%;
                border-radius: 7px;
                background-color: var(--gray-black);
                border: none;
                color: white;
                cursor: pointer;
                box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.103);
                position: relative;
                overflow: hidden;
                transition-duration: 0.3s;

                &:active {
                    transform: translate(5px, 5px);
                    transition-duration: 0.3s;
                }
            }
        }

        .detailCard {
            display: flex;
            flex-direction: column;
            width: 100%;


            input,
            span {
                border: none;
                color: var(--gray-light);
                background: rgb(238, 238, 238);
                box-shadow: 0 0 30px #1414140a;
                border-radius: 7px;
                border: 1px solid rgba(0, 0, 0, 0.125);
                padding: 4px;

                &:focus {
                    outline: none;
                }
            }

            input {
                background: var(--white);
                color: rgb(8, 8, 8);
            }

            span {
                cursor: not-allowed;
            }

            label {
                font-size: small;
                margin-bottom: 1%;
                margin-top: 2%;
                color: var(--gray-light);
            }
        }

    }

    .dismiss {
        position: absolute;
        right: 3%;
        top: 1%;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0.5rem 1rem;
        background-color: transparent;
        color: black;
        border: 0px;
        font-size: 1.2rem;
        font-weight: 300;
        width: 40px;
        height: 40px;
        border-radius: 10px;
        transition: .3s ease;
        z-index: 100;
    }

    .dismiss:hover {
        background-color: #e03131;
        color: #fff;
    }

}


.card {
    overflow: hidden;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: left;
    border-radius: 0.5rem;
    max-width: 30rem;

    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
    background-color: #fff;
    z-index: 30;
}


.header {
    padding: 1.25rem 1rem 1rem 1rem;
}

.image {
    display: flex;
    margin-left: auto;
    margin-right: auto;
    background-color: #e2feee;
    flex-shrink: 0;
    justify-content: center;
    align-items: center;
    width: 4rem;
    height: 4rem;
    border-radius: 9999px;
    animation: animate .6s linear alternate-reverse infinite;
    transition: .6s ease;

    img {
        height: 80%;
        width: 80%;
    }
}


.content {
    margin-top: 0.75rem;
    text-align: center;
    gap: 10px;
    white-space: normal;
}

.title {
    color: #121212;
    font-size: 1rem;
    font-weight: 500;
    padding: 0 5px;
    line-height: 1.5rem;

    &:nth-child(2n) {
        color: #858585;
        font-size: 0.9rem;

    }
}

.message {
    margin-top: 0.5rem;
    color: #595b5f;
    font-size: 0.875rem;
    line-height: 1.25rem;
}

.actions {
    margin: 0.75rem 1rem;
}

.history {
    display: inline-flex;
    padding: 0.5rem 1rem;
    margin-top: 10%;
    background-color: #3f50ea;
    color: #ffffff;
    font-size: 1rem;
    line-height: 1.5rem;
    font-weight: 500;
    justify-content: center;
    width: 100%;
    border-radius: 0.375rem;
    border: none;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);

    &:hover {
        filter: contrast(1.2);
    }
}



@keyframes animate {
    from {
        transform: scale(1);
    }

    to {
        transform: scale(1.09);
    }
}

.errorMsg {
    font-size: smaller;
    margin-bottom: 0;
}

.spinnerContainer {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.spinner {
    width: 56px;
    height: 56px;
    display: grid;
    border: 4px solid #0000;
    border-radius: 50%;
    border-right-color: #299fff;
    animation: tri-spinner 1s infinite linear;
}

.spinner::before,
.spinner::after {
    content: "";
    grid-area: 1/1;
    margin: 2px;
    border: inherit;
    border-radius: 50%;
    animation: tri-spinner 2s infinite;
}

.spinner::after {
    margin: 8px;
    animation-duration: 3s;
}

@keyframes tri-spinner {
    100% {
        transform: rotate(1turn);
    }
}

.loader {
    color: #4a4a4a;
    font-family: "Poppins", sans-serif;
    font-weight: 500;
    font-size: 23px;
    -webkit-box-sizing: content-box;
    box-sizing: content-box;
    height: 40px;
    padding: 10px 10px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    border-radius: 8px;
}

.words {
    overflow: hidden;
}

.word {
    display: block;
    height: 100%;
    color: #299fff;
    text-align: center;
    animation: cycle-words 3s infinite;
}

@keyframes cycle-words {
    0% {
        -webkit-transform: translateY(50%);
        transform: translateY(50%);
    }

    5% {
        -webkit-transform: translateY(0%);
        transform: translateY(0%);
    }

    30% {
        -webkit-transform: translateY(0%);
        transform: translateY(0%);
    }

    45% {
        -webkit-transform: translateY(-105%);
        transform: translateY(-105%);
    }

    65% {
        -webkit-transform: translateY(-105%);
        transform: translateY(-105%);
    }

    75% {
        -webkit-transform: translateY(-200%);
        transform: translateY(-200%);
    }


    90% {
        -webkit-transform: translateY(-200%);
        transform: translateY(-200%);
    }

    100% {
        -webkit-transform: translateY(-300%);
        transform: translateY(-300%);
    }
}
</style>

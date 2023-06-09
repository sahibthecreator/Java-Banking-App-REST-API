<script setup>
import TransactionTabWidget from '../TransactionTabWidget.vue';
</script>

<template>
  <div class="transactionPanel">
    <div class="controls">
      <b-input type="text" v-model="input" placeholder="Search Transactions" />
      <div class="buttons">
        <b-button variant="black">Last week</b-button>
        <b-button variant="black">Last Month</b-button>
        <b-button variant="black">Last Year</b-button>
        <div class="downloadStatementBtnWrapper">
          <b-button variant="black" @click="exportBtnsEnabled = !exportBtnsEnabled">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-download"
              viewBox="0 0 20 20">
              <path
                d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z" />
              <path
                d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z" />
            </svg>
            Download Bank Statement
          </b-button>
          <div class="choice" v-if="exportBtnsEnabled">
            <b-button variant="dark_primary" @click="exportPdf">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                class="bi bi-filetype-pdf" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                  d="M14 4.5V14a2 2 0 0 1-2 2h-1v-1h1a1 1 0 0 0 1-1V4.5h-2A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v9H2V2a2 2 0 0 1 2-2h5.5L14 4.5ZM1.6 11.85H0v3.999h.791v-1.342h.803c.287 0 .531-.057.732-.173.203-.117.358-.275.463-.474a1.42 1.42 0 0 0 .161-.677c0-.25-.053-.476-.158-.677a1.176 1.176 0 0 0-.46-.477c-.2-.12-.443-.179-.732-.179Zm.545 1.333a.795.795 0 0 1-.085.38.574.574 0 0 1-.238.241.794.794 0 0 1-.375.082H.788V12.48h.66c.218 0 .389.06.512.181.123.122.185.296.185.522Zm1.217-1.333v3.999h1.46c.401 0 .734-.08.998-.237a1.45 1.45 0 0 0 .595-.689c.13-.3.196-.662.196-1.084 0-.42-.065-.778-.196-1.075a1.426 1.426 0 0 0-.589-.68c-.264-.156-.599-.234-1.005-.234H3.362Zm.791.645h.563c.248 0 .45.05.609.152a.89.89 0 0 1 .354.454c.079.201.118.452.118.753a2.3 2.3 0 0 1-.068.592 1.14 1.14 0 0 1-.196.422.8.8 0 0 1-.334.252 1.298 1.298 0 0 1-.483.082h-.563v-2.707Zm3.743 1.763v1.591h-.79V11.85h2.548v.653H7.896v1.117h1.606v.638H7.896Z" />
              </svg>
              To PDF
            </b-button>
            <b-button variant="info" @click="exportCsv">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                class="bi bi-filetype-csv" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                  d="M14 4.5V14a2 2 0 0 1-2 2h-1v-1h1a1 1 0 0 0 1-1V4.5h-2A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v9H2V2a2 2 0 0 1 2-2h5.5L14 4.5ZM3.517 14.841a1.13 1.13 0 0 0 .401.823c.13.108.289.192.478.252.19.061.411.091.665.091.338 0 .624-.053.859-.158.236-.105.416-.252.539-.44.125-.189.187-.408.187-.656 0-.224-.045-.41-.134-.56a1.001 1.001 0 0 0-.375-.357 2.027 2.027 0 0 0-.566-.21l-.621-.144a.97.97 0 0 1-.404-.176.37.37 0 0 1-.144-.299c0-.156.062-.284.185-.384.125-.101.296-.152.512-.152.143 0 .266.023.37.068a.624.624 0 0 1 .246.181.56.56 0 0 1 .12.258h.75a1.092 1.092 0 0 0-.2-.566 1.21 1.21 0 0 0-.5-.41 1.813 1.813 0 0 0-.78-.152c-.293 0-.551.05-.776.15-.225.099-.4.24-.527.421-.127.182-.19.395-.19.639 0 .201.04.376.122.524.082.149.2.27.352.367.152.095.332.167.539.213l.618.144c.207.049.361.113.463.193a.387.387 0 0 1 .152.326.505.505 0 0 1-.085.29.559.559 0 0 1-.255.193c-.111.047-.249.07-.413.07-.117 0-.223-.013-.32-.04a.838.838 0 0 1-.248-.115.578.578 0 0 1-.255-.384h-.765ZM.806 13.693c0-.248.034-.46.102-.633a.868.868 0 0 1 .302-.399.814.814 0 0 1 .475-.137c.15 0 .283.032.398.097a.7.7 0 0 1 .272.26.85.85 0 0 1 .12.381h.765v-.072a1.33 1.33 0 0 0-.466-.964 1.441 1.441 0 0 0-.489-.272 1.838 1.838 0 0 0-.606-.097c-.356 0-.66.074-.911.223-.25.148-.44.359-.572.632-.13.274-.196.6-.196.979v.498c0 .379.064.704.193.976.131.271.322.48.572.626.25.145.554.217.914.217.293 0 .554-.055.785-.164.23-.11.414-.26.55-.454a1.27 1.27 0 0 0 .226-.674v-.076h-.764a.799.799 0 0 1-.118.363.7.7 0 0 1-.272.25.874.874 0 0 1-.401.087.845.845 0 0 1-.478-.132.833.833 0 0 1-.299-.392 1.699 1.699 0 0 1-.102-.627v-.495Zm8.239 2.238h-.953l-1.338-3.999h.917l.896 3.138h.038l.888-3.138h.879l-1.327 4Z" />
              </svg>
              To CSV
            </b-button>
          </div>

        </div>

      </div>
    </div>

    <div class="card transactions">
      <TransactionTabWidget v-for="(transaction, index) in transactions" :key="index" :transaction="transaction"
        :currentAccount="currentAccount" :accounts="accounts" />
    </div>
  </div>
</template>

<script>
import jsPDF from 'jspdf';
import 'jspdf-autotable';

export default {
  name: 'TransactionTab',
  data() {
    return {
      input: null,
      transactions: null,
      exportBtnsEnabled: false,
    };
  },
  mounted() {
    this.getTransactions();
  },
  methods: {
    async getTransactions() {
      let transactions = await this.$store.dispatch(
        'getTransactionsByUserId',
        this.$store.getters.getUserId
      );
      this.transactions = transactions;
    },

    exportPdf() {
      const doc = new jsPDF();
      const headers = ['Transaction Type', 'From Account', 'To Account', 'Amount', 'Description', 'Date'];
      const rows = this.transactions.map((transaction) => [
        transaction.typeOfTransaction,
        transaction.fromAccount,
        transaction.toAccount,
        "€ " + transaction.amount.toString(),
        transaction.description || '',
        transaction.dateOfExecution,
      ]);

      const columnWidths = ['auto', 'auto', 'auto', 'auto', 'auto', 'auto'];

      doc.setFontSize(22);
      doc.text('WAVR Bank Statement', doc.internal.pageSize.getWidth() / 2, 10, { align: 'center' });

      doc.autoTable({
        head: [headers],
        body: rows,
        startY: 20,
        tableWidth: 'auto',
        columnWidth: columnWidths,
        theme: 'grid',
        headStyles: { fillColor: "#233da3" },
      });

      doc.save('wavr_statement.pdf');
    },
    exportCsv() {
      const headers = ['Transaction Type', 'From Account', 'To Account', 'Amount (Euro)', 'Description', 'Date'];
      const rows = this.transactions.map((transaction) => [
        transaction.typeOfTransaction,
        transaction.fromAccount,
        transaction.toAccount,
        transaction.amount.toString(),
        transaction.description || '',
        transaction.dateOfExecution,
      ]);

      let csvContent = `${headers.join(',')}\n`;
      csvContent += rows.map(row => row.join(',')).join('\n');

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });

      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'wavr_statement.csv';
      link.style.display = 'none';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

  },


};
</script>

<style lang="scss" scoped>
.card {
  padding: 35px;
  background: var(--white);
  box-shadow: 0 0 30px #1414140a;
  border-radius: 10px;
}

.transactionPanel {
  width: 90vw;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 30px;

  .controls {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    padding: 25px 0px;

    .buttons {
      display: flex;
      flex-direction: row;
      gap: 20px;

      .downloadStatementBtnWrapper {
        position: relative;

        .choice {
          position: absolute;
          display: flex;
          gap: 1rem;
          margin: 0.5rem 0;
          z-index: 2;
          width: 100%;

          button {
            font-size: smaller;
            width: 50%;
          }
        }
      }

      button {
        width: fit-content;
        white-space: nowrap;
        display: flex;
        align-items: center;
        padding-inline: 20px;

        svg {
          margin-right: 0.6rem;
        }
      }
    }
  }
}
</style>
